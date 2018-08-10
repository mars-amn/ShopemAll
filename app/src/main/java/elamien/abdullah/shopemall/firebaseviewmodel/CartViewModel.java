package elamien.abdullah.shopemall.firebaseviewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import elamien.abdullah.shopemall.model.User;
import elamien.abdullah.shopemall.utils.Constants;


public class CartViewModel extends ViewModel {
    private MutableLiveData<User> mUser;

    public LiveData<User> getUser() {
        if (mUser == null) {
            mUser = new MutableLiveData<>();
            loadUserCart();
        }
        return mUser;
    }

    private void loadUserCart() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child(Constants.USER_CHILD);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        reference.orderByChild(Constants.USER_ID_CHILD).equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User userCart = dataSnapshot.getValue(User.class);
                mUser.postValue(userCart);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User userCart = dataSnapshot.getValue(User.class);
                mUser.postValue(userCart);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                User userCart = dataSnapshot.getValue(User.class);
                mUser.postValue(userCart);
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
