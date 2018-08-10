package elamien.abdullah.shopemall.wishlistviewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import elamien.abdullah.shopemall.database.WishlistDatabase;
import elamien.abdullah.shopemall.model.Product;

public class WishlistProductDetailsViewModel extends ViewModel {
    private LiveData<Product> mProduct;
    private int mProductCode;
    private WishlistDatabase mDatabase;

    public WishlistProductDetailsViewModel(int code, WishlistDatabase database) {
        this.mProductCode = code;
        this.mDatabase = database;
    }

    public LiveData<Product> getProduct() {
        if (mProduct == null) {
            mProduct = new MutableLiveData<>();
            getItemFromDatabase();
        }
        return mProduct;
    }

    private void getItemFromDatabase() {
        mProduct = mDatabase.mWishListProductsDao().getProduct(mProductCode);
    }
}