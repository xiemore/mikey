package com.example.web.config;

import com.example.web.modules.User;
import com.example.web.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取前端传来的token
        String accessToken = (String) token.getPrincipal();

        //redis缓存中这样存值， key为token，value为username
        //根据token去缓存里查找用户名
        String user_name = stringRedisTemplate.opsForValue().get(accessToken);
        if (user_name == null) {
            //查找的用户名为空，即为token失效
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        User user = userService.find_by_user_name(user_name);
        if (user == null) {
            throw new UnknownAccountException("用户不存在!");
        }

        //此方法需要返回一个AuthenticationInfo类型的数据
        // 因此返回一个它的实现类SimpleAuthenticationInfo,将user以及获取到的token传入它可以实现自动认证
        return new SimpleAuthenticationInfo(user, accessToken, "");
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //从认证那里获取到用户对象User
        User user = (User) principals.getPrimaryPrincipal();
        //此方法需要一个AuthorizationInfo类型的返回值，因此返回一个它的实现类SimpleAuthorizationInfo
        //通过SimpleAuthorizationInfo里的addStringPermissions()设置用户的权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<String> permission_list = userService.select_user_role(user.getUser_name());
        simpleAuthorizationInfo.addStringPermissions(permission_list);
        return simpleAuthorizationInfo;
    }
}
