package com.Mike12138210;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    @Test
    public void testGen(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","张三");

        // 生成JWT对象
        String token = JWT.create()
                .withClaim("user",claims) // 添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12)) //添加过期时间 12小时
                .sign(Algorithm.HMAC256("mike12138210")); // 指定算法，配置秘钥
        System.out.println(token);
    }

    @Test
    public void testParse(){
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjE3NzQ1Mjg3MTR9." +
                "lNmwDh9wZ_Team5UiEhXT8sD4eCp6AAtNwR9fqw7MsU";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("mike12138210")).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token); // 验证token，生成一个解析后的JWT对象
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));
    }
}