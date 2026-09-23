package cloud.mmda.core.clients;

import cloud.mmda.core.services.exceptions.OperationFailedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
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
public class FileClient extends ApiClient {
    private static final Log logger = LogFactory.getLog(FileClient.class);
    private static final String FILE_SERVICES = "mmda-file";

    public FileClient(String clientName, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
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
                    logger.error(filename);
                    logError("上传",e.getCause(),filename,url,uriVariables);
                    return Mono.just("");
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
               return Flux.error(new OperationFailedException(e.getLocalizedMessage()));
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

}