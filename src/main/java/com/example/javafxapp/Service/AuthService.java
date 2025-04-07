package com.example.javafxapp.Service;

import com.example.javafxapp.Repository.AuthRepository;
import com.example.javafxapp.Model.Account;

public class AuthService {
    private AuthRepository authRepository = new AuthRepository() ;

    public boolean Login(String account_name , String password ) {
        return authRepository.login(account_name,password) ;
    }

    public int signUp(Account account) {
        return authRepository.signUp(account) ;
    }
}
