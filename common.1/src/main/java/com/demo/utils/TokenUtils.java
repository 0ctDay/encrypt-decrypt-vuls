package com.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

/**
 * @Description:
 * @Author: Rosh
 * @Date: 2021/8/11 14:21
 */
public final class TokenUtils {


    private static final String SIGN_KEY = "STREAM_TOKEN";


    /**
     *jwt 有效期为3天
     */
    private static final Long JWT_EXPIRE_TIME_LONG = 1000 * 60 * 60 * 24 * 3L;


    private TokenUtils() {

    }

    public static String createToken(String str) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = createSecretKey();

        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(str)
                .signWith(signatureAlgorithm, secretKey);

        long expMillis = System.currentTimeMillis() + JWT_EXPIRE_TIME_LONG;
        Date exp = new Date(expMillis);

        //token过期时间
        builder.setExpiration(exp);

        return builder.compact();
    }

    private static SecretKey createSecretKey() {
        byte[] encodedKey = Base64.decodeBase64(SIGN_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }


    public static Claims getClaim(String token) {
        SecretKey secretKey = createSecretKey();
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ignored) {
            return null;
        }
    }


}
