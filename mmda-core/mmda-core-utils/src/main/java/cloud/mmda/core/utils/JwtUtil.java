package cloud.mmda.core.utils;

import io.jsonwebtoken.*;

import java.security.*;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
public final class JwtUtil {
    private static final String ALGORITHM = "RSA"; // 加密算法
    private static final int KEY_SIZE = 2048; // 密钥长度
    private static final long EXPIRATION_TIME = 3600; // 令牌过期时间（单位：秒）
    private static final SecureRandom RANDOM = new SecureRandom();

    private JwtUtil(){}
    // 生成RSA公私钥对
    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE, RANDOM);
        return keyPairGenerator.generateKeyPair();
    }

    // 生成RSA签名算法
    private static SignatureAlgorithm getSignatureAlgorithm() {
        return SignatureAlgorithm.RS256;
    }

    // 生成JWT令牌
    public static String generateToken(Long validTo, String privateKeyStr,Map<String, Object> claims) throws Exception {

        PrivateKey privateKey = RsaUtil.getPrivateKey(privateKeyStr);
       String token="";
       if (BaseUtil.isNullOrZero(validTo)){
            token = Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                   .setClaims(claims)
                   .signWith(getSignatureAlgorithm(), privateKey)
                   .compact();
       }else {
           Date date=new Date(validTo);
            token = Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                   .setClaims(claims)
                   .setExpiration(date)
                   .signWith(getSignatureAlgorithm(), privateKey)
                   .compact();
       }

        return token;
    }

    // 验证JWT令牌
    public static boolean validateToken(String publicKeyStr,String token) throws Exception {
        try {
            PublicKey  publicKey = RsaUtil.getPublicKey(publicKeyStr);
            // 解析JWT
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);

            // 验证头部
            Header header = claimsJws.getHeader();
            if (!"JWT".equals(header.getType())) {
                return false;
            }

            // 验证签名
            if (!claimsJws.getSignature().equals(token.split("\\.")[2])) {
                return false;
            }

            // 验证过期时间
            Date exp = claimsJws.getBody().getExpiration();
            if (exp != null && Instant.now().isAfter(exp.toInstant())) {
                return false;
            }

            return true;
        }catch (Exception e){
            return false;
        }

    }

    // 解析JWT令牌
    public static Claims parseToken(String publicKeyStr,String token) throws Exception {
        PublicKey  publicKey = RsaUtil.getPublicKey(publicKeyStr);
        Claims claims = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getBody();
        return claims;
    }
    // 验证JWT令牌过期
    public static boolean validateTokenExpiration(String publicKeyStr,String token) throws Exception {
        try {
            PublicKey  publicKey = RsaUtil.getPublicKey(publicKeyStr);
            // 解析JWT
            Claims claims = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
//                    .setSigningKey(publicKey)
//                    .parseClaimsJws(token)
//                    .getBody();
            // 验证过期时间
            Date exp = claims.getExpiration();
            if (exp != null && Instant.now().isAfter(exp.toInstant())) {
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }

    }

}
