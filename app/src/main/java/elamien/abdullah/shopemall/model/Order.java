package elamien.abdullah.shopemall.model;

import java.util.List;


public class Order {
    private String userName;
    private String userAddress;
    private int userZip;
    private String userPhone;
    private String userEnteredEmail;
    private String userEmail;
    private String userId;
    private User userObject;
    private String orderKey;
    private float productsPriceWithoutDiscount;
    private CreditCard creditCard;
    private float totalDiscountPrice;
    private float totalPriceAfterDiscount;
    private List<String> orderStatus;
    private String estimatedTime;

    public Order(String userName, String userAddress, int userZip, String userPhone,
                 String userEnteredEmail, String userEmail, String userId, User userObject,
                 float productsPriceWithoutDiscount, CreditCard creditCard, float totalDiscountPrice,
                 float totalPriceAfterDiscount) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userZip = userZip;
        this.userPhone = userPhone;
        this.userEnteredEmail = userEnteredEmail;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userObject = userObject;
        this.productsPriceWithoutDiscount = productsPriceWithoutDiscount;
        this.creditCard = creditCard;
        this.totalDiscountPrice = totalDiscountPrice;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }

    public Order() {
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public User getUserObject() {
        return userObject;
    }

    public void setUserObject(User userObject) {
        this.userObject = userObject;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public List<String> getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(List<String> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public float getTotalPriceAfterDiscount() {
        return totalPriceAfterDiscount;
    }

    public void setTotalPriceAfterDiscount(float totalPriceAfterDiscount) {
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }

    public float getTotalDiscountPrice() {
        return totalDiscountPrice;
    }

    public void setTotalDiscountPrice(float totalDiscountPrice) {
        this.totalDiscountPrice = totalDiscountPrice;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public int getUserZip() {
        return userZip;
    }

    public void setUserZip(int userZip) {
        this.userZip = userZip;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEnteredEmail() {
        return userEnteredEmail;
    }

    public void setUserEnteredEmail(String userEnteredEmail) {
        this.userEnteredEmail = userEnteredEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public float getProductsPriceWithoutDiscount() {
        return productsPriceWithoutDiscount;
    }

    public void setProductsPriceWithoutDiscount(float productsPriceWithoutDiscount) {
        this.productsPriceWithoutDiscount = productsPriceWithoutDiscount;
    }


    public CreditCard getCreditCard() {
        return creditCard;
    }

}
