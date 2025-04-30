package com.example.javafxapp.Service;

import com.example.javafxapp.Repository.RolePermissionRepository;

import java.util.List;

public class RolePermissionService {
    private RolePermissionRepository rolePermissionRepository = new RolePermissionRepository();

    public void addRolePermission(int roleId, int permissionId) {
        rolePermissionRepository.add(roleId, permissionId);
    }


    public void deleteRolePermission(int roleId , int permissionId) {
        rolePermissionRepository.delete(roleId , permissionId);
    }

    public List<Integer> getAllRolePermission(int roleId) {
        return rolePermissionRepository.getAll(roleId);
    }

}
