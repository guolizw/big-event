package com.zw;

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

        Map<String,Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username","wanghe");


        String token = JWT.create()
                .withClaim("user", claims)//添加有效载荷
                //当前毫秒值往后延迟
                .withExpiresAt(new Date(System.currentTimeMillis()*1000*60*5))//添加过期时间
                .sign(Algorithm.HMAC256("zw"));//设置算法，配置密钥

        System.out.println(token);
    }

    @Test
    public void testParse(){
        //定义字符串来模拟用户传递的令牌
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IndhbmdoZSJ9LCJleHAiOjUxMTQ4MTYxMjY0MDYwMH0" +
                ".vhnA_eRRbboIo8tkr6-ZVBqZ_DgzpjlMjlVq2qQrtSA";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("zw")).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token); //生成一个解析后的jwt对象

        Claim user = decodedJWT.getClaim("user");

        System.out.println(user);


    }
}