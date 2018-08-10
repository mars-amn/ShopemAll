package elamien.abdullah.shopemall.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.List;


@Entity(tableName = "WishList")
public class Product {
    @Ignore
    private List<String> availableColors;
    private String name;
    private String description;
    private String category;
    @PrimaryKey
    private int code;
    private float price;
    private String image;
    @Ignore
    private List<String> allImages;
    private float sale;
    @Exclude
    @ColumnInfo(name = "createdAt")
    private Date timeStamp;
    @Ignore
    private int shoppingCartQuantity;

    public Product(String name, String description, String category, int code, float price, String image, float sale, Date timeStamp) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.code = code;
        this.price = price;
        this.image = image;
        this.sale = sale;
        this.timeStamp = timeStamp;
    }

    @Ignore
    public Product(Product mProduct) {
        this.name = mProduct.getName();
        this.description = mProduct.getDescription();
        this.category = mProduct.getCategory();
        this.code = mProduct.getCode();
        this.price = mProduct.getPrice();
        this.image = mProduct.getImage();
        this.sale = mProduct.getSale();
        this.timeStamp = new Date();
    }

    @Ignore
    public Product(String name, String description, String category, String imageUrl, int code, float price, float sale) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.code = code;
        this.price = price;
        this.image = imageUrl;
        this.sale = sale;
    }

    @Ignore
    public Product() {
    }


    public List<String> getAvailableColors() {
        return availableColors;
    }

    public void setAvailableColors(List<String> availableColors) {
        this.availableColors = availableColors;
    }

    public int getShoppingCartQuantity() {
        return shoppingCartQuantity;
    }

    public void setShoppingCartQuantity(int shoppingCartQuantity) {
        this.shoppingCartQuantity = shoppingCartQuantity;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getSale() {
        return sale;
    }

    public void setSale(float sale) {
        this.sale = sale;
    }

    public List<String> getAllImages() {
        return allImages;
    }

    public void setAllImages(List<String> allImages) {
        this.allImages = allImages;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}