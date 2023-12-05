package com.td.server.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.td.server.dto.base.LoginUser;
import com.td.server.pojo.SysRole;
import com.td.server.pojo.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Td
 * @desc: 自定义权限逻辑
 */
@Component("td")
public class PermissionUtils {
    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    private static final String SUPER_ADMIN = "admin";

    private static final String ROLE_DELIMETER = ",";

    private static final String PERMISSION_DELIMETER = ",";

    private static final String LAST_PAGE_STR = "page";

    private static final String LAST_PAGE_PERM_STR = "list";

    @Autowired
    protected HttpServletRequest request;

    private static final String PERM_REGEX = "^\\$+.*\\}$";

    private static final Pattern pattern = Pattern.compile(PERM_REGEX);

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermission(String permission) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        // 如果上下文路径长度大于1，则从请求URI中移除上下文路径
        if (contextPath.length() > 1) {
            path = path.replace(contextPath, "");
        }

        //如果权限以$开头，且以}结尾，则是动态权限
        if (pattern.matcher(permission).matches()) {
            //删掉开头的/
            if (path.indexOf("/") == 0) {
                path = path.substring(1);
            }
            String[] split = path.split("/");
            //如果请求路径最后一位是page，则替换为list
            if (split[split.length - 1].equals(LAST_PAGE_STR)) {
                path = path.replace(LAST_PAGE_STR, LAST_PAGE_PERM_STR);
            }
            // 如果路径包含四个部分，将前三部分用":"连接起来
            if (split.length == 4) {
                path = split[0] + ":" + split[1] + ":" + split[2];
            }
            // 正则匹配将"/"替换为":"
            permission = permission.replaceAll(PERM_REGEX, path).replaceAll("/", ":");
        }

        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermission(String permission) {
        return hasPermission(permission) != true;
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermission(String permissions) {
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(PERMISSION_DELIMETER)) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getRoles())) {
            return false;
        }
        for (SysRole sysRole : loginUser.getRoles()) {
            String roleKey = sysRole.getRoleKey();
            if (SUPER_ADMIN.equals(roleKey) || roleKey.equals(StringUtils.trim(role))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role) {
        return hasRole(role) != true;
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roles) {
        if (StringUtils.isEmpty(roles)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getRoles())) {
            return false;
        }
        for (String role : roles.split(ROLE_DELIMETER)) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }

    public static void main(String[] args) {
        String permission = "${sys:role:list}";
        String path = "/system/role/list";

        if (pattern.matcher(permission).matches()) {
            if (path.indexOf("/") == 0) {
                path = path.substring(1);
            }
            String[] split = path.split("/");
            //如果请求路径最后一位是page，则替换为list
            if (split[split.length - 1].equals(LAST_PAGE_STR)) {
                path = path.replace(LAST_PAGE_STR, LAST_PAGE_PERM_STR);
            }
            // 如果路径包含四个部分，将前三部分用":"连接起来
            if (split.length == 4) {
                path = split[0] + ":" + split[1] + ":" + split[2];
            }
            permission = permission.replaceAll(PERM_REGEX, path).replaceAll("/", ":");
        }

        System.out.println(permission);
        System.out.println(path);

    }
}
