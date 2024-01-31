
package com.magic.ssh.service;

import com.magic.ssh.entity.security.JwtUser;
import com.magic.ssh.entity.security.SysUser;
import com.magic.ssh.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;




    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser queryUser = sysUserMapper.getUser(username);

        if (null == queryUser) {
            throw new UsernameNotFoundException("用户账号：" + username + "，不存在");
        } else {

            log.info(queryUser.toString());
            Set<GrantedAuthority> authorities = new HashSet<>();
            //获取该用户所有的权限信息
//            sysUserMapper.getRoleByUserId(queryUser.getUserId()).forEach(role -> {
//                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
//            });
            JwtUser jwtUser=new JwtUser(username, queryUser.getPassword());
            return jwtUser;
        }

    }
}