package com.struggle.sys.mapper;

import com.struggle.sys.model.RoleMenu;
import com.struggle.sys.pojo.SysRole;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SysRoleMapper {
    List<SysRole> listPage(SysRole sysRole, RowBounds rowBounds);

    List<SysRole> listLikePage(SysRole sysRole, RowBounds rowBounds);

    List<SysRole> list(SysRole SysRole);

    SysRole getById(Long id);

    SysRole getOne(SysRole sysRole);

    Long count(SysRole SysRole);

    Long countLike(SysRole SysRole);

    // 根据用户Id获取用户角色
    List<SysRole> getUserRoleByUserId(Long userId);

    // 根据角色Id获取
    List<RoleMenu> getRoleMenuByRoleId(Long roleId);

    void save(SysRole SysRole);

    void saveBatch(List<SysRole> list);

    void update(SysRole SysRole);

    void updateBatch(List<SysRole> list);

    void delete(Long id);

    // 更新角色菜单
    void updateRoleMenu(RoleMenu roleMenu);
    // 保存角色菜单
    void saveRoleMenu(RoleMenu roleMenu);
    // 删除角色菜单
    void deleteRoleMenu(Long id);
}