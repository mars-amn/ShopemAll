package elamien.abdullah.shopemall.model;

import java.util.List;


public class Cart {
    private List<Product> cartProducts;
    private String userUId;

    public Cart() {

    }

    public Cart(List<Product> cartProducts, String userUId) {
        this.cartProducts = cartProducts;
        this.userUId = userUId;
    }


    public List<Product> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<Product> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public String getUserUId() {
        return userUId;
    }

    public void setUserUId(String userUId) {
        this.userUId = userUId;
    }
}