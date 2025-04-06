package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private AccountRepository accountRepository = new AccountRepository() ;

    // add account .
    public void addAccount(Account account) {
        accountRepository.add(account);
    }

    // update account .
    public void updateAccount(Account account) {
        accountRepository.update(account);
    }


    // delete account .
    public void deleteAccount(int accountId) {
        accountRepository.delete(accountId);
    }

    // get all account .
    public List<Account> getAllAccounts() {
        return accountRepository.getAll() ;
    }

    // get nameAccount()
    public boolean existsNameAccount(String accountName){
       return accountRepository.existsNameAccount(accountName) ;
    }

    // find role by account_id .
    public Account findAccountByID(int account_id) {
        return accountRepository.findAccountByID(account_id) ;
    }

    // find role by account_name.
    public Account findAccountByName(String account_name) {
        return accountRepository.findAccountByName(account_name) ;
    }

    // check nameAccount is exists and different it .
    public boolean existsNameAccountOther(int account_id , String accountName){
        return accountRepository.existsNameAccountOther(account_id,accountName) ;
    }
}
