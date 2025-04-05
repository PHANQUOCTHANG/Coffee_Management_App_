package com.example.javafxapp.Service;

import com.example.javafxapp.Model.Account;
import com.example.javafxapp.Repository.AccountRepository;

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

    // get account
    public Account getAccount(int accountId) {
        return accountRepository.getAccount(accountId) ;
    }

    // get nameAccount()
    public boolean existsNameAccount(String accountName){
       return accountRepository.existsNameAccount(accountName) ;
    }
}
