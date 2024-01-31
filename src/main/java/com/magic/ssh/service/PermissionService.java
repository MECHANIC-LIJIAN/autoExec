package com.magic.ssh.service;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface PermissionService {

    boolean checkPermission(HttpServletRequest request, Authentication authentication);
}
