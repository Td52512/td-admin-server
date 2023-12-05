package com.td.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.td.server.pojo.SysMenu;
import com.td.server.service.SysMenuService;
import com.td.server.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author 陈文杰
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2023-12-04 16:44:45
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

}




