package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.InformationUser;
import com.example.javafxapp.Repository.InformationUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InformationUserService {
    private InformationUserRepository informationUserRepository = new InformationUserRepository() ;

    // add informationUser .
    public void add(InformationUser informationUser) {
        informationUserRepository.add(informationUser);
    }

    // update informationUser .
    public void update(InformationUser informationUser) {
        informationUserRepository.update(informationUser);
    }
    public InformationUser getInformationUserByAccountId(int accountId) {
        return informationUserRepository.getInformationUserByAccountId(accountId) ;
    }
}
