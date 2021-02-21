package com.struggle.sys.mapper;

import com.struggle.sys.pojo.SysRole;
import com.struggle.sys.pojo.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SysUserMapper {
    List<SysUser> listPage(SysUser sysUser, RowBounds rowBounds);

    List<SysUser> listLikePage(SysUser sysUser, RowBounds rowBounds);

    List<SysUser> list(SysUser SysUser);

    SysUser getById(Long id);

    SysUser getOne(SysUser sysUser);

    Long count(SysUser SysUser);

    Long countLike(SysUser SysUser);

    void save(SysUser SysUser);

    void saveBatch(List<SysUser> list);

    void update(SysUser SysUser);

    void updateBatch(List<SysUser> list);

    void delete(Long id);
}