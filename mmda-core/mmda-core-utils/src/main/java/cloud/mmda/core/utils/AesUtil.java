package cloud.mmda.core.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class AesUtil {
    /**
     * 加解密统一编码方式
     */
    private final static String ENCODING = "utf-8";

    /**
     * 加解密方式
     */
    private final static String ALGORITHM  = "AES";

    /**
     *加密模式及填充方式
     */
    private final static String PATTERN = "AES/ECB/pkcs5padding";

    /**
     * AES的密钥长度
     */
    private static final Integer SECRET_KEY_LENGTH = 128;

    public static final String PASSWORD = "syicanfly";


    /**
     * 秘钥生成来源
     */
    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";



    public static String getSecretKey() throws NoSuchAlgorithmException {
        //生成指定算法密钥的生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        //linux系统下填充
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        //设置上密钥种子
        random.setSeed(ALLCHAR.getBytes());

        keyGenerator.init(SECRET_KEY_LENGTH, random);

        //生成密钥
        SecretKey secretKey = keyGenerator.generateKey();
        //转换成AES的密钥
        SecretKeySpec secretKeySpec= new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
        return secretKey.getEncoded().toString();
    }

    /**
     * 生成AES密钥对象
     *
     * @throws NoSuchAlgorithmException
     */
    public static String generateAESKey() {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * AES加密
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText, String key) throws Exception {
        if (key == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        SecretKey secretKey = new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM);
        // AES加密采用pkcs5padding填充
        Cipher cipher = Cipher.getInstance(PATTERN);
        //用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        //执行加密操作
        byte[] encryptData = cipher.doFinal(plainText.getBytes(ENCODING));
        return Base64.getEncoder().encodeToString(encryptData);
    }


    /**
     * AES解密
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String plainText, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM);
        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance(PATTERN);
        // 初始化密码器（解密模型）
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // 解密数据, 返回明文
        byte[] encryptData = cipher.doFinal(Base64.getDecoder().decode(plainText));
        return new String(encryptData,ENCODING);
    }

    /**
     * AES解密
     * @param bytes
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] bytes, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM);
        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance(PATTERN);
        // 初始化密码器（解密模型）
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // 解密数据, 返回明文
        byte[] encryptData = cipher.doFinal(bytes);
        return encryptData;
    }

    /**
     * 通过文件输入流加密文件并输出到指定路径
     * CipherOutputStream进行加密数据
     */
    public static void aesFile(String sourceFilePath, String destFilePath, String key, int mode) throws Exception {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);
        if (sourceFile.exists() && !sourceFile.isFile()) {
            throw new IllegalArgumentException("加密源文件不存在");
        }
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(destFile);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM);
        Cipher cipher = Cipher.getInstance(PATTERN);
        cipher.init(mode, secretKeySpec);
        // 对输出流包装
        CipherOutputStream cout = new CipherOutputStream(out, cipher);
        byte[] cache = new byte[1024];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {
            cout.write(cache, 0, nRead);
            cout.flush();
        }
        cout.close();
        out.close();
        in.close();
    }

    /**
     * 通过文件输入流加密文件并输出到指定路径
     * CipherInputStream进行加密数据
     */
    public static void aesFileForInput(String sourceFilePath, String destFilePath, String key, int mode) throws Exception {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);
        if (sourceFile.exists() && sourceFile.isFile()) {
            throw new IllegalArgumentException("加密源文件不存在");
        }
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(destFile);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM);
        Cipher cipher = Cipher.getInstance(PATTERN);
        cipher.init(mode, secretKeySpec);
        // 对输入流包装
        CipherInputStream cin = new CipherInputStream(in, cipher);

        byte[] cache = new byte[1024];
        int nRead = 0;
        while ((nRead = cin.read(cache)) != -1) {
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        cin.close();
        in.close();
    }

    /**
     * AES加密方法，此处使用AES-128-ECB加密模式，key需要为16位
     * @param sKey
     * @return
     */
    public static void encryptStream(ByteArrayOutputStream source, OutputStream out, String sKey) throws Exception{
        long start = System.currentTimeMillis();

            // 判断Key是否正确
            if (sKey == null || sKey.length() != 16) {
                throw new RuntimeException("Key为空或长度不为16位");
            }
            byte[] raw = sKey.getBytes(ENCODING);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(PATTERN);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            CipherOutputStream cout = new CipherOutputStream(out, cipher);
            cout.write(source.toByteArray());
            cout.close();
            source.close();

    }

    /**
     * AES加密方法，此处使用AES-128-ECB加密模式，key需要为16位
     * @param sKey
     * @return
     */
    public static void encryptStream(InputStream in,  OutputStream out, String sKey) throws Exception{

//        // 判断Key是否正确
//        if (sKey == null || sKey.length() != 16) {
//            throw new RuntimeException("Key为空或长度不为16位");
//        }
        byte[] raw = sKey.getBytes(ENCODING);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance(PATTERN);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        // 对输入流包装
        CipherInputStream cin = new CipherInputStream(in, cipher);

        byte[] cache = new byte[1024];
        int nRead = 0;
        while ((nRead = cin.read(cache)) != -1) {
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        cin.close();
        in.close();

    }

    /**
     * AES解密方法，此处使用AES-128-ECB加密模式，key需要为16位
     * @param sKey
     * @return
     */
    public static void decryptStream(InputStream in, OutputStream out, String sKey) throws Exception{

//        long start = System.currentTimeMillis();

//            // 判断Key是否正确
//            if (sKey == null || sKey.length() != 16) {
//                throw new RuntimeException("Key为空或长度不为16位");
//            }
            byte[] raw = sKey.getBytes(ENCODING);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
            Cipher cipher = Cipher.getInstance(PATTERN);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            // 先用base64解密
//            LOG.info("AES解密耗时：" + (System.currentTimeMillis() - start) + "ms");

            CipherOutputStream cout = new CipherOutputStream(out, cipher);
            byte[] cache = new byte[1024];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                cout.write(cache, 0, nRead);
                cout.flush();
            }
            cout.close();
            in.close();
            out.close();
    }

    public static void aesEncryptFile(String sourceFilePath, String destFilePath, String key) throws Exception {
        aesFile(sourceFilePath, destFilePath, key, Cipher.ENCRYPT_MODE);
    }
    public static void aesDecryptFile(String sourceFilePath, String destFilePath, String key) throws Exception {
        aesFile(sourceFilePath, destFilePath, key, Cipher.DECRYPT_MODE);
    }

    public static void aesEncryptFileForInput(String sourceFilePath, String destFilePath, String key) throws Exception {
        aesFileForInput(sourceFilePath, destFilePath, key, Cipher.ENCRYPT_MODE);
    }
    public static void aesDecryptFileForInput(String sourceFilePath, String destFilePath, String key) throws Exception {
        aesFileForInput(sourceFilePath, destFilePath, key, Cipher.DECRYPT_MODE);
    }

    public static Cipher getCipher( String sKey) throws Exception{
        long start = System.currentTimeMillis();

        // 判断Key是否正确
        if (sKey == null || sKey.length() != 16) {
            throw new RuntimeException("Key为空或长度不为16位");
        }
        byte[] raw = sKey.getBytes(ENCODING);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance(PATTERN);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher;

    }


}
