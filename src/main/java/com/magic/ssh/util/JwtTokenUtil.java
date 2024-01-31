package com.magic.ssh.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.magic.ssh.entity.security.JwtUser;

import java.util.Date;
import java.util.HashMap;

public class JwtTokenUtil {

    public static final String TOKEN_PREFIX="token";

    /**
     * 过期时间为15分钟
     */
    private static final long EXPIRE_TIME = 60*60*1000;

    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "and0X3ZhbGlkYXRpb25fY29uZmlnX2tleQ==";

    /**
     * 生成签名,60分钟后过期
     * @return
     */
    public static String sign(JwtUser user){
        //过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        return JWT.create()
                .withHeader(header)
                .withClaim("username",user.getUsername())
                .withIssuer("Jack")
                .withIssuedAt(new Date())
                .withExpiresAt(date)
                .sign(algorithm);
    }


    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);

            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token){
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败

        }
        return audience;
    }


    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name){
        return JWT.decode(token).getClaim(name);
    }
}