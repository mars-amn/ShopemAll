package elamien.abdullah.shopemall.firebaseviewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ProductsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String categoryChosen;
    private int mProductCode;

    public ProductsViewModelFactory(String categoryChosen) {
        this.categoryChosen = categoryChosen;
    }

    public ProductsViewModelFactory(int code, String category) {
        this.mProductCode = code;
        this.categoryChosen = category;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductsViewModel.class)) {
            if (mProductCode != 0) {
                return (T) new ProductsViewModel(mProductCode, categoryChosen);
            } else {
                return (T) new ProductsViewModel(categoryChosen);
            }
        } else {
            throw new IllegalArgumentException("This is unknown ViewModel class :/ ");
        }
    }
}
