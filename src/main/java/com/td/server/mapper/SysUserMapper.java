package com.td.server.mapper;

import com.td.server.pojo.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
* @author 陈文杰
* @description 针对表【sys_user(用户信息表)】的数据库操作Mapper
* @createDate 2023-12-04 16:44:45
* @Entity com.td.server.pojo.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<String> selectUserPermissionByUserId(Long id);
}




