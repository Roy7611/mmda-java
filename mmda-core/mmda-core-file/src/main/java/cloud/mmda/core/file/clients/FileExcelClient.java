package cloud.mmda.core.file.clients;

import cloud.mmda.core.file.util.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;

/**
 * 文件客户端
 *
 * 微服务可直接注入此类，调用其接口上传下载文件。
 *
 * @author lcl
 * @version 4.0.0
 * @since 2025/1/2
 *
 */
public class FileExcelClient extends ApiClient {
    private static final Log logger = LogFactory.getLog(FileExcelClient.class);
    private static final String FILE_SERVICES = "mmda-file";

    public FileExcelClient(String clientName, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        super(clientName, FILE_SERVICES, lbFunction);
    }


    /**
     * 上传文件
     *
     * @return
     */
    public Mono<String> uploadFile(String dir, Long id,String filename, File file) {
        return uploadFileWeb(file,filename,dir,id);
    }

    /**
     * 上传文件
     *
     * @return
     */
    public Mono<File> uploadExcelFile(String dir, Long id,String filename, File file) {
        return uploadExcelFileWeb(file,filename,dir,id);
    }

    /**
     * 上传文件
     *
     * @return
     */
    public Mono<String> uploadFile(String dir, String id,String filename, File file) {
        return uploadFileWeb(file,filename,dir,id);
    }

    public Mono<String> uploadFileWeb(File file,String filename, Object...uriVariables){
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("file", new FileSystemResource(file));
        String url="/files/{dir}/{id}";
        return super.webClient.post()
                .uri(uriBuilder -> uriBuilder.path(url)
                                .queryParam("filename", filename)
                                .build(uriVariables))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(data))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.BadRequest.class, e->{
                    logger.error(filename+e.getMessage());
                    logError("上传",e.getCause(),filename,url,uriVariables);
                    return Mono.just("");
                })
//                .subscribe(System.out::println)
        ;
    }

    public Mono<File> uploadExcelFileWeb(File file,String filename, Object...uriVariables){
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("file", new FileSystemResource(file));
        String url="/files/excel/{dir}/{id}";

        Flux<DataBuffer> dataBufferFlux = super.webClient.post()
                .uri(uriBuilder -> uriBuilder.path(url)
                        .queryParam("filename", filename)
                        .build(uriVariables))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(data))
                .retrieve()
                .bodyToFlux(DataBuffer.class);
        return DataBufferUtils.join(dataBufferFlux)
                .map(dataBuffer -> {
                    FileUtil.dataBufferToFile(dataBuffer, file);
                    return file;
                })


//                .subscribe(System.out::println)
                ;
    }

    /**
     * 上传文件
     *
     * @return
     */
    public Flux<String> uploadFiles(List<File> files,String dir, Long id){
        return uploadFilesWeb(files,dir,id);
    }

    public Flux<String> uploadFilesWeb(List<File> files, Object...uriVariables){

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        for (File file : files) {
            byte[] fileBytes = new byte[0];
            try {
                fileBytes = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
               e.printStackTrace();
            }
            // 添加文件部分
            bodyBuilder.part("files", fileBytes)
                    .headers(headers -> {
                        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                        headers.setContentDispositionFormData("files", file.getName());
                    });
        }
        String url="/files/{dir}/{id}/multi";
        return super.webClient.post()
                .uri(url,uriVariables)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToFlux(String.class)
//                .subscribe(System.out::println)
                ;
    }

    public Mono<Boolean> deleteFile(String filename){

        int apiIndex=filename.indexOf("api")+3;
//        String url="/files/{dir}/{id}/{filename}";
        String url=filename.substring(apiIndex);

        return super.webClient.delete()
                .uri(uriBuilder -> uriBuilder.path(url)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false);
    }





/**
 * 下载文件
 *
 * @return
 */
    public  Flux<byte[]>  downloadFiles(List<String> urls) {

        Flux<byte[]> fileFlux = Flux.fromIterable(urls).flatMapSequential(url -> WebClient.create().get().uri(url).retrieve().bodyToMono(byte[].class));

        return fileFlux;
    }
    /**
     * 下载文件
     *
     * @return
     */
    public  Mono<byte[]>  downloadFile(String url) {
        Mono<byte[]> fileMono = WebClient.create().get().uri(url).retrieve().bodyToMono(byte[].class).doOnError(e->logTrace(e.getMessage()));
        return fileMono;
    }
    private static final ExchangeStrategies STRATEGIES = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
            .build();
    public Mono<byte[]> downloadFile(String originalUrl,boolean encoded) {
        // 解析并编码 URL
        URI encodedUri = UriComponentsBuilder.fromUriString(originalUrl)
                .build(encoded)  // true 表示保留已编码部分
                .toUri();
        return WebClient.builder()
                .exchangeStrategies(STRATEGIES)
                .build()
                .get()
                .uri(encodedUri)
                .retrieve()
                .bodyToMono(byte[].class)
                .doOnError(e -> logTrace(e.getMessage()));
    }
}