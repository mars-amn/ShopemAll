package elamien.abdullah.shopemall.firebaseviewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


public class OrderViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private String mOrderKey;

    public OrderViewModelFactory(String mOrderKey) {
        this.mOrderKey = mOrderKey;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderViewModel.class)) {
            return (T) new OrderViewModel(mOrderKey);
        } else {
            throw new IllegalArgumentException("This is unknown ViewModel class :/ ");
        }
    }
}
