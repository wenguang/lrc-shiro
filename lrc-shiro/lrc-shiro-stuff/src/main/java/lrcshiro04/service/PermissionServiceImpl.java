package lrcshiro04.service;

import lrcshiro04.dao.PermissionDao;
import lrcshiro04.dao.PermissionDaoImpl;
import lrcshiro04.entity.Permission;

public class PermissionServiceImpl implements PermissionService {
    private PermissionDao permissionDao = new PermissionDaoImpl();

    public Permission createPermission(Permission permission) {
        return permissionDao.createPermission(permission);
    }

    public void deletePermission(Long permissionId) {
        permissionDao.deletePermission(permissionId);
    }
}
