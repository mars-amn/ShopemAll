package elamien.abdullah.shopemall.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.activities.CartActivity;
import elamien.abdullah.shopemall.adapters.WishlistAdapter;
import elamien.abdullah.shopemall.database.WishlistDatabase;
import elamien.abdullah.shopemall.firebaseviewmodel.CartViewModel;
import elamien.abdullah.shopemall.model.Product;
import elamien.abdullah.shopemall.model.User;
import elamien.abdullah.shopemall.wishlistviewmodel.WishlistProductsViewModel;

public class WishlistFragment extends Fragment {
    @BindView(R.id.favoriteItemsRecyclerView)
    RecyclerView mFavoriteRecyclerView;
    @BindView(R.id.emptyWishlistState)
    View mEmptyState;
    private WishlistAdapter mWishlistAdapter;
    private TextView mCartBadgeTextView;

    public WishlistFragment() {
    }


    public static Fragment getInstance() {
        return new WishlistFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View wishlistedProductsView = inflater.inflate(R.layout.fragment_wishlist_products, container, false);
        ButterKnife.bind(this, wishlistedProductsView);
        setupWishlistProducts();
        setupSwipeToDelete();
        return wishlistedProductsView;
    }

    private void setupWishlistProducts() {
        WishlistProductsViewModel model = ViewModelProviders.of(this).get(WishlistProductsViewModel.class);
        model.getWishlistedProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products.size() == 0) {
                    showEmptyState();
                } else {
                    hideEmptyState();
                    mWishlistAdapter = new WishlistAdapter(products, getContext());
                    mFavoriteRecyclerView.setAdapter(mWishlistAdapter);
                }

            }
        });
    }


    private void showEmptyState() {
        mFavoriteRecyclerView.setVisibility(View.GONE);
        mEmptyState.setVisibility(View.VISIBLE);
    }

    private void hideEmptyState() {
        mEmptyState.setVisibility(View.GONE);
        mFavoriteRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setupSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                WishlistDatabase database = WishlistDatabase.getDatabase(getContext());
                List<Product> products = mWishlistAdapter.getProducts();
                database.mWishListProductsDao().deleteProduct(products.get(position));
                mWishlistAdapter.notifyItemRemoved(position);
            }
        }).attachToRecyclerView(mFavoriteRecyclerView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wishlist_menu, menu);
        final MenuItem cartMenuItem = menu.findItem(R.id.wishlistCartMenuItemAction);
        View actionView = cartMenuItem.getActionView();
        mCartBadgeTextView = actionView.findViewById(R.id.cartMenuItemBadge);
        setupCartViewModel();

        mCartBadgeTextView = actionView.findViewById(R.id.cartMenuItemBadge);
        setupCartViewModel();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(cartMenuItem);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.wishlistCartMenuItemAction:
                launchCartActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchCartActivity() {
        Intent intent = new Intent(getContext(), CartActivity.class);
        startActivity(intent);
    }

    private void setupCartViewModel() {
        CartViewModel model = ViewModelProviders.of(this).get(CartViewModel.class);
        model.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mCartBadgeTextView.setText(String.valueOf(user.getProductsCount()));
            }
        });
    }
}