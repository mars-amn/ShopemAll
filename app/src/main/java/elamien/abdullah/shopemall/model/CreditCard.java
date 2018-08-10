package elamien.abdullah.shopemall.model;


public class CreditCard {
    private String creditCardNumber;
    private int expMonth;
    private int expYear;
    private int cvv;

    public CreditCard() {
    }

    public CreditCard(String creditCardNumber, int expMonth, int expYear, int cvv) {
        this.creditCardNumber = creditCardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public int getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(int expMonth) {
        this.expMonth = expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
}
