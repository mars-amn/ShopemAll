package elamien.abdullah.shopemall.wishlistviewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import elamien.abdullah.shopemall.database.WishlistDatabase;


public class WishlistProductDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int mItemCode;
    private WishlistDatabase mDatabase;


    public WishlistProductDetailsViewModelFactory(int code, WishlistDatabase database) {
        this.mItemCode = code;
        this.mDatabase = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WishlistProductDetailsViewModel.class)) {
            return (T) new WishlistProductDetailsViewModel(mItemCode, mDatabase);
        } else {
            throw new IllegalArgumentException("This is unknown ViewModel class :/ ");
        }
    }
}