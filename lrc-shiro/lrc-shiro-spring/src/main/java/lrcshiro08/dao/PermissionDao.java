package lrcshiro08.dao;


import lrcshiro08.entity.Permission;

public interface PermissionDao {

    public Permission createPermission(Permission permission);
    public void deletePermission(Long permissionId);

}
