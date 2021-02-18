package com.struggle.sys.mapper;

import com.struggle.sys.model.dto.MenuRoleDTO;
import com.struggle.sys.pojo.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SysMenuMapper {


    List<SysMenu> listPage(SysMenu sysMenu, RowBounds rowBounds);

    List<SysMenu> listLikePage(SysMenu sysMenu, RowBounds rowBounds);

    List<SysMenu> list(SysMenu SysMenu);

    SysMenu getById(Long id);

    SysMenu getOne(SysMenu sysMenu);

    Long count(SysMenu SysMenu);

    Long countLike(SysMenu SysMenu);

    // 根据用户Id获取菜单信息
    List<SysMenu> getUserMenuById(Long userId);

    // 获取菜单信息
    List<SysMenu> getMenuList();

    // 获取菜单对应的角色
    List<MenuRoleDTO> getMenuWithRole();

    void save(SysMenu SysMenu);

    void update(SysMenu SysMenu);

    void updateBatch(List<SysMenu> list);

    void delete(Long id);

    void deleteBatch(@Param("ids") Long[] ids);
}