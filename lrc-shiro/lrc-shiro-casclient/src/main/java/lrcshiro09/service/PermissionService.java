package lrcshiro09.service;

import lrcshiro09.entity.Permission;


public interface PermissionService {
    public Permission createPermission(Permission permission);
    public void deletePermission(Long permissionId);
}
