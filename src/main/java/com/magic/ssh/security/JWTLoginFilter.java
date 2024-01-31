
package com.magic.ssh.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.ssh.entity.security.JwtUser;
import com.magic.ssh.entity.security.SysUser;
import com.magic.ssh.util.JwtTokenUtil;
import com.magic.ssh.util.Result;
import com.magic.ssh.util.ResultCode;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证用户名密码正确后 生成一个token并将token返回给客户端
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * 验证操作 接收并解析用户凭证
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            SysUser sysUser = new ObjectMapper().readValue(request.getInputStream(), SysUser.class);
            logger.info(sysUser.toString());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword()));
        } catch (Exception e) {

            e.printStackTrace();
            throw new BadCredentialsException("");
        }
    }

    /**
     * 验证【成功】后调用的方法
     * 若验证成功 生成token并返回
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        JwtUser user = (JwtUser) authResult.getPrincipal();

        // 从User中获取权限信息
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        // 创建Token
        String token = JwtTokenUtil.sign(user);

        // 设置编码 防止乱码问题
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // 处理编码方式 防止中文乱码
        response.setContentType("text/json;charset=utf-8");
        // 将反馈塞到HttpServletResponse中返回给前台
        Map<String,Object> map=new HashMap<>();
        map.put("accessToken",token);
        map.put("auth",authorities);

        SecurityContextHolder.getContext().setAuthentication(authResult);

        new ObjectMapper().writeValue(response.getWriter(),
                Result.success(map));

    }

    /**
     * 验证【失败】调用的方法
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String returnData = "";
        // 账号过期
        if (failed instanceof AccountExpiredException) {
            returnData = "账号过期";
        }
        // 密码错误
        else if (failed instanceof BadCredentialsException) {
            returnData = "用户名或密码错误";
        }
        // 密码过期
        else if (failed instanceof CredentialsExpiredException) {
            returnData = "密码过期";
        }
        // 账号不可用
        else if (failed instanceof DisabledException) {
            returnData = "账号不可用";
        }
        //账号锁定
        else if (failed instanceof LockedException) {
            returnData = "账号锁定";
        }
        // 用户不存在
        else if (failed instanceof InternalAuthenticationServiceException) {
            returnData = "用户不存在";
        }
        // 其他错误
        else {
            returnData = "未知异常";
        }
        // 处理编码方式 防止中文乱码
        response.setContentType("text/json;charset=utf-8");
        // 将反馈塞到HttpServletResponse中返回给前台
        new ObjectMapper().writeValue(response.getWriter(),
                Result.build(ResultCode.USER_NOTLOGGED_IN, returnData));
    }
}