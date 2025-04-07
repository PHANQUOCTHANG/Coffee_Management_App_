package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Permission;
import com.example.javafxapp.Repository.PermissionRepositoty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionService {

    private PermissionRepositoty permissionRepositoty = new PermissionRepositoty() ;
    // add permission .
    public void addPermission(Permission permission) {
        permissionRepositoty.add(permission);
    }

    // update permission
    public void updatePermission(Permission permission) {
       permissionRepositoty.update(permission);
    }

    // delete permission .
    public void deletePermission(int permissionId) {
        permissionRepositoty.delete(permissionId);
    }

    // get all permission .
    public List<Permission> getAllPermission(){
        return permissionRepositoty.getAll() ;
    }

    // find permission by permission_id .
    public Permission findPermissionByID(int permissionId) {
        return permissionRepositoty.findPermissionByID(permissionId) ;
    }

    // find permission by permission_name .
    public Permission findPermissionByName(String permissionName) {
        return permissionRepositoty.findPermissionByName(permissionName) ;
    }
}
