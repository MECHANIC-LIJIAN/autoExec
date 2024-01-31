package com.magic.ssh.service.impl;

import com.magic.ssh.entity.security.JwtUser;
import com.magic.ssh.service.PermissionService;
import com.magic.ssh.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;


@Slf4j
@Component
public class PermissionServiceImpl implements PermissionService {
    private static final PathMatcher pathmatcher = new AntPathMatcher();

    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean checkPermission(HttpServletRequest request, Authentication authentication) {

        String token = request.getHeader("accessToken");

        String uri = request.getRequestURI();

        if (token == null) {
            return false;
        }
        if (JwtTokenUtil.verify(token)) {
            String username = JwtTokenUtil.getClaimByName(token, "username").asString();

            log.info(username);
            if (username.equals("root")){
                return true;
            }

            JwtUser jwtUser=userService.getUserDetailInfoByUserName(username);

            Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
            log.info(jwtUser.toString());

            Iterator it = authorities.iterator();
            while (it.hasNext()) {
                if (pathmatcher.match(it.next().toString(), uri)) {
                    return true;
                }
            }
        }
//        throw new AccountExpiredException("");
        return false;
    }
}
