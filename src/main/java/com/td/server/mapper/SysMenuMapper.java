package com.td.server.mapper;

import com.td.server.pojo.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author 陈文杰
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
 * @createDate 2023-12-04 16:44:45
 * @Entity com.td.server.pojo.SysMenu
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {


    List<SysMenu> selectAllRouterMenus();

    List<SysMenu> selectRouterMenusByUserId(Long userId);
}




