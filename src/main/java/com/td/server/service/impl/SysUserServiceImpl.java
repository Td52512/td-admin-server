package com.td.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.td.server.dto.constant.Constants;
import com.td.server.mapper.SysMenuMapper;
import com.td.server.pojo.SysMenu;
import com.td.server.pojo.SysUser;
import com.td.server.service.SysUserService;
import com.td.server.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 陈文杰
 * @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
 * @createDate 2023-12-04 16:44:45
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public Set<String> getUserPermission(Long userId) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (userId == 1L) {
            perms.add("*:*:*");
        } else {
            List<String> permission = sysUserMapper.selectUserPermissionByUserId(userId);
            System.out.println(permission);
            for (String perm : permission) {
                if (StringUtils.isNotBlank(perm)) {
                    perms.addAll(Arrays.asList(perm.trim().split(",")));
                }
            }
        }
        return perms;
    }

    @Override
    public List<SysMenu> getUserRouterMenus(Long userId) {
        List<SysMenu> menus = null;
        if (userId == 1L) {
            menus = sysMenuMapper.selectAllRouterMenus();
        } else {

            menus = sysMenuMapper.selectRouterMenusByUserId(userId);

        }
        return getChildPerms(menus, 0);
    }


    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

}




