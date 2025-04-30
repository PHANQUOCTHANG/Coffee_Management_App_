package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Model.Role;
import com.example.javafxapp.Repository.RoleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleService {

    private RoleRepository roleRepository = new RoleRepository() ;

    // add role .
    public void addRole(Role role) {
        roleRepository.add(role);
    }


    // update role .
    public void updateRole(Role role){
        roleRepository.update(role);
    }

    // delete role .
    public void deleteRole(int roleId) {
        roleRepository.delete(roleId);
    }

    // get all role .
    public List<Role> getAllRole() {
        return roleRepository.getAll() ;
    }

    // find role by category_id .
    public Role findRoleByID(int role_id) {
        return roleRepository.findByID(role_id) ;
    }

    // find role by category_name.
    public Role findRoleByName(String role_name) {
        return roleRepository.findByName(role_name) ;
    }
}
