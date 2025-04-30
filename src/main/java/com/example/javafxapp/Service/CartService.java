package com.example.javafxapp.Service;

import com.example.javafxapp.Model.Cart;
import com.example.javafxapp.Repository.CartRepository;

import java.util.List;

public class CartService {
    private CartRepository cartRepository = new CartRepository() ;
    // add cart .
    public void add(Cart cart) {
        cartRepository.add(cart);
    }

    // update cart .
    public void update(Cart cart) {
        cartRepository.update(cart);
    }

    // delete cart .
    public void delete(int cartId) {
        cartRepository.delete(cartId);
    }

    // get all cart .
    public List<Cart> getAll(){
        return cartRepository.getAll() ;
    }

    // find cart by cart_id .
    public Cart findByID(int cartId) {
        return cartRepository.findByID(cartId) ;
    }

    // find cart by cart_name .
    public Cart findByName(String cart_name) {
        return cartRepository.findByName(cart_name) ;
    }

    // check account have cart ?
    public Integer checkAccountHaveCart (int account_id) {
        return cartRepository.checkAccountHaveCart(account_id) ;
    }
}
