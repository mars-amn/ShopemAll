package elamien.abdullah.shopemall.wishlistviewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import elamien.abdullah.shopemall.database.WishlistDatabase;
import elamien.abdullah.shopemall.model.Product;

public class WishlistProductsViewModel extends AndroidViewModel {
    private LiveData<List<Product>> mWishlistedProducts;

    public WishlistProductsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Product>> getWishlistedProducts() {
        if (mWishlistedProducts == null) {
            mWishlistedProducts = new MutableLiveData<>();
            loadWishlistedProducts();
        }
        return mWishlistedProducts;
    }

    private void loadWishlistedProducts() {
        WishlistDatabase database = WishlistDatabase.getDatabase(getApplication());
        mWishlistedProducts = database.mWishListProductsDao().getAllProducts();
    }
}
