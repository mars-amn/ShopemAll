package elamien.abdullah.shopemall.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.fragments.WishlistFragment;

public class WishlistProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_products);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.wishlist_activity_label);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wishlistedProductsFragmentContainer, WishlistFragment.getInstance())
                    .commit();
        }
    }
}
