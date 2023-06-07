package com.ruoyi.framework.shiro.realm;

import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.jwt.auth.JwtToken;
import com.ruoyi.framework.jwt.utils.JwtUtils;
import com.ruoyi.framework.shiro.service.LoginService;
import com.ruoyi.project.system.menu.service.IMenuService;
import com.ruoyi.project.system.role.service.IRoleService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.domain.UserStatus;
import com.ruoyi.project.system.user.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm 处理登录 权限
 *
 * @author ruoyi
 */
public class UserRealm extends AuthorizingRealm
{
    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

//    @Autowired
//    private ISysMenuService menuService;

    private IMenuService menuService;

//    @Autowired
//    private ISysRoleService roleService;

    @Autowired
    private IRoleService roleService;

//    @Autowired
//    private SysLoginService loginService;

    @Autowired
    private LoginService loginService;

//    @Autowired
//    private IUserService userService;
    @Autowired
    private IUserService userService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0)
    {
        User user = ShiroUtils.getSysUser();
        // 角色列表
        Set<String> roles = new HashSet<String>();
        // 功能列表
        Set<String> menus = new HashSet<String>();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            info.addRole("admin");
            info.addStringPermission("*:*:*");
        }
        else
        {
            roles = roleService.selectRoleKeys(user.getUserId());
            menus = menuService.selectPermsByUserId(user.getUserId());
            // 角色加入AuthorizationInfo认证对象
            info.setRoles(roles);
            // 权限加入AuthorizationInfo认证对象
            info.setStringPermissions(menus);
        }
        return info;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException
    {
        if (authenticationToken instanceof JwtToken)
        {
            JwtToken jwtToken = (JwtToken) authenticationToken;
            String token = jwtToken.getToken();
            String username = JwtUtils.getUserName(token);
            if (username == null)
            {
                throw new AccountException("token 验证失败");
            }
            User user = userService.selectUserByLoginName(username);
            if (user == null)
            {
                throw new AuthenticationException("用户数据不存在");
            }

            try
            {
                JwtUtils.verify(username, user.getPassword(), jwtToken.getToken());

                if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
                {
                    throw new UserDeleteException();
                }

                if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
                {
                    throw new UserBlockedException();
                }
            }
            catch (Exception e)
            {
                log.info("对用户[" + username + "]进行jwt登录验证..验证未通过{}", e.getMessage());
                throw new AuthenticationException(e.getMessage(), e);
            }

            return new SimpleAuthenticationInfo(user, null, getName());
        }
        else
        {
            UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
            String username = upToken.getUsername();
            String password = "";
            if (upToken.getPassword() != null)
            {
                password = new String(upToken.getPassword());
            }

            User user = null;
            try
            {
                user = loginService.login(username, password);
            }
            catch (CaptchaException e)
            {
                throw new AuthenticationException(e.getMessage(), e);
            }
            catch (UserNotExistsException e)
            {
                throw new UnknownAccountException(e.getMessage(), e);
            }
            catch (UserPasswordNotMatchException e)
            {
                throw new IncorrectCredentialsException(e.getMessage(), e);
            }
            catch (UserPasswordRetryLimitExceedException e)
            {
                throw new ExcessiveAttemptsException(e.getMessage(), e);
            }
            catch (UserBlockedException e)
            {
                throw new LockedAccountException(e.getMessage(), e);
            }
            catch (RoleBlockedException e)
            {
                throw new LockedAccountException(e.getMessage(), e);
            }
            catch (Exception e)
            {
                log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
                throw new AuthenticationException(e.getMessage(), e);
            }
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, null, getName());
            return info;
        }
    }

    /**
     * 清理指定用户授权信息缓存
     */
    public void clearCachedAuthorizationInfo(Object principal)
    {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        this.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清理所有用户授权信息缓存
     */
    public void clearAllCachedAuthorizationInfo()
    {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null)
        {
            for (Object key : cache.keys())
            {
                cache.remove(key);
            }
        }
    }
}
