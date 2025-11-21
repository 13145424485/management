package com.doc.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {
    //颁发者
    private String issuer;
    //密钥
    private String secret;
    //过期时间
    private int expiration;

    /**
     * 生成token
     * @param map
     * @return
     */

    public String generateToken(Map<String,String> map){
        //设置令牌的过期时间  日历类 获取一个 Calendar 对象
        //用来处理日期和时间
        Calendar instance = Calendar.getInstance();
        //设置失效时间
        instance.add(Calendar.MINUTE, expiration);
        //创建JWT Bulier
        JWTCreator.Builder builder = JWT.create();
        //payload
        map.forEach((k,v) -> {;
            builder.withClaim(k,v);
        });

        //指定令牌的过期时间
        //这行代码是利用 JWTCreator.Builder 链式链式 调用构建并生成 JWT 令牌的核心逻辑，通过连续设置令牌属性并最终签名，完成 Token 的生成
        String token = builder.withIssuer(issuer)
                //设置 iss 标准声明（令牌颁发者，如系统名称，对应你配置中的 jwt.issuer）
                .withIssuedAt(new Date())
                //设置 iat 标准声明（令牌签发时间，当前时间）。
                .withExpiresAt(instance.getTime())
                //设置 exp 标准声明（令牌过期时间，通过 Calendar 计算的未来时间）。
                .sign(Algorithm.HMAC256(secret));
                //生成 Token 字符串
        return token;
    }
    /**
     * 验证令牌是否合法
     * @param token
     * @return
     */
    public boolean verify(String token){
        try {
            JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        }
        catch (JWTVerificationException e){
            return false;
        }
        return true;
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public DecodedJWT jwtDecode(String token){
        try {
            return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("token签名错误!");
        } catch (AlgorithmMismatchException e) {
            throw new RuntimeException("token算法不匹配!");
        } catch (TokenExpiredException e) {
            throw new RuntimeException("token过期!");
        } catch (Exception e) {
            throw new RuntimeException("token解析失败!");
        }
    }




}
