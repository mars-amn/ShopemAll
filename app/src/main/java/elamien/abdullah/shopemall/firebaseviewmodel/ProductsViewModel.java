package elamien.abdullah.shopemall.firebaseviewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import elamien.abdullah.shopemall.model.Categories;
import elamien.abdullah.shopemall.model.Product;
import elamien.abdullah.shopemall.utils.Constants;


public class ProductsViewModel extends ViewModel {
    private String mCategory;
    private MutableLiveData<List<Product>> mCategoryProducts;
    private MutableLiveData<Product> mSpecificProduct;
    private int mProductCode;

    public ProductsViewModel(int mProductCode, String mCategory) {
        this.mProductCode = mProductCode;
        this.mCategory = mCategory;
    }

    public ProductsViewModel(String category) {
        mCategory = category;
    }

    public MutableLiveData<Product> getSpecificProduct() {
        if (mSpecificProduct == null) {
            mSpecificProduct = new MutableLiveData<>();
            loadProductWithCode();
        }
        return mSpecificProduct;
    }

    private void loadProductWithCode() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child(Constants.CATEGORY_CHILD);
        reference.orderByChild(Constants.CATEGORY_NAME_CHILD).equalTo(mCategory).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Categories categories = dataSnapshot.getValue(Categories.class);
                List<Product> items = categories.getCategoryProducts();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getCode() == mProductCode) {
                        mSpecificProduct.setValue(items.get(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Product>> getCategoryProducts() {
        if (mCategoryProducts == null) {
            mCategoryProducts = new MutableLiveData<>();
            loadCategoryItems();
        }
        return mCategoryProducts;
    }

    private void loadCategoryItems() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child(Constants.CATEGORY_CHILD);
        reference.orderByChild(Constants.CATEGORY_NAME_CHILD).equalTo(mCategory).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Categories category = dataSnapshot.getValue(Categories.class);
                mCategoryProducts.postValue(category.getCategoryProducts());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

