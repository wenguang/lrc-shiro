package lrcshiro04.dao;

import lrcshiro04.entity.Permission;

public interface PermissionDao {

    public Permission createPermission(Permission permission);
    public void deletePermission(Long permissionId);

}
