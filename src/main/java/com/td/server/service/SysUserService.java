package com.td.server.service;

import com.td.server.pojo.SysMenu;
import com.td.server.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
* @author 陈文杰
* @description 针对表【sys_user(用户信息表)】的数据库操作Service
* @createDate 2023-12-04 16:44:45
*/
public interface SysUserService extends IService<SysUser> {


    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getUserPermission(Long userId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> getUserRouterMenus(Long userId);
}
