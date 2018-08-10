package elamien.abdullah.shopemall.model;

import java.util.HashMap;
import java.util.Map;


public class User {
    private String name;
    private String userId;
    private Cart cart;
    private int productsCount;

    public User() {
    }

    public User(String name, String userId, Cart cart, int productsCount) {
        this.name = name;
        this.userId = userId;
        this.cart = cart;
        this.productsCount = productsCount;
    }

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cart", cart);
        result.put("userUId", userId);
        result.put("name", name);
        return result;
    }
}
