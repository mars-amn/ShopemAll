package elamien.abdullah.shopemall.model;

import java.util.List;

public class Categories {
    private String categoryName;
    private List<Product> categoryProducts;
    private String categoryImage;

    public Categories() {
    }

    public Categories(String categoryName, List<Product> categoryProducts, String categoryImage) {
        this.categoryName = categoryName;
        this.categoryProducts = categoryProducts;
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Product> getCategoryProducts() {
        return categoryProducts;
    }

    public void setCategoryProducts(List<Product> categoryProducts) {
        this.categoryProducts = categoryProducts;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
}