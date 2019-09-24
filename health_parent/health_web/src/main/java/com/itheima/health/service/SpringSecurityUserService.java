package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SpringSecurityUserService
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/21 9:22
 * @Version V1.0
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    // 认证
    @Reference
    UserService userService;

    // 认证、授权：当执行login.do的时候，执行到loadUserByUsername的方法，并传递用户名
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用用户名，从数据库中查询，返回User对象
        com.itheima.health.pojo.User user = userService.findUserByUsername(username);
        // 输入的用户名有误
        if(user==null){
            return null; // 抛出异常，表示：“登录名输入有误”（org.springframework.security.authentication.InternalAuthenticationServiceException）
        }
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if(roles!=null && roles.size()>0) {
            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getKeyword())); // 角色
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null && permissions.size() > 0) {
                    for (Permission permission : permissions) {
                        list.add(new SimpleGrantedAuthority(permission.getKeyword())); // 权限
                    }
                }
            }
        }
        // 输入的用户名没有错误，比对密码（SpringSecurity自动完成）
        // 如果输入的密码有误，抛出异常，表示：“输入密码有误”(org.springframework.security.authentication.BadCredentialsException:)
        return new User(user.getUsername(),user.getPassword(),list);
    }
}
