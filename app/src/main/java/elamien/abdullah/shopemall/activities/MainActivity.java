package elamien.abdullah.shopemall.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.tapadoo.alerter.Alerter;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.GlideApp;
import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.adapters.CategoriesAdapter;
import elamien.abdullah.shopemall.adapters.ProductImagesAdapter;
import elamien.abdullah.shopemall.firebaseviewmodel.CartViewModel;
import elamien.abdullah.shopemall.firebaseviewmodel.CategoriesViewModel;
import elamien.abdullah.shopemall.model.Categories;
import elamien.abdullah.shopemall.model.Product;
import elamien.abdullah.shopemall.model.User;
import elamien.abdullah.shopemall.wishlistviewmodel.WishlistProductsViewModel;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String CATEGORIES_STATE_KEY = "elamien.abdullah.shopemall.activities";
    final List<Product> mFTProducts = new ArrayList<>();
    @BindView(R.id.categoriesRecyclerView)
    RecyclerView mCategoriesRecyclerView;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigationView)
    NavigationView mNavigationView;
    @BindView(R.id.featuredProductsRecyclerView)
    RecyclerView mFTProductsRecyclerView;
    @BindView(R.id.featuredProductNameTextSwitcher)
    TextSwitcher mFTProductNameTextSwitcher;
    @BindView(R.id.featuredProductPrice)
    TextSwitcher mFTProductPrice;
    @BindView(R.id.featuredProductsLabel)
    TextView mFTProductsLabel;
    @BindView(R.id.loadingIndicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.retryConnectionButton)
    Button mRetryButton;
    private ImageView mProfileImage;
    private ImageView mProfileHeader;
    private TextView mUserNameTextView;
    private TextView mUserEmailTextView;
    private TextView mCartBadgeTextView;
    private TextView mWishlistProductsBadgeTextView;
    private CategoriesAdapter mCategoriesAdapter;
    private CardSliderLayoutManager mFTLayoutManager;
    private int mFTRecyclerViewPosition;
    private Parcelable mLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupHeader();
        if (savedInstanceState != null) {
            mLayoutState = savedInstanceState.getParcelable(CATEGORIES_STATE_KEY);
        }
        setupViews();
        setupNavigationDrawer();
        setupCartMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLayoutState = mCategoriesRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(CATEGORIES_STATE_KEY, mLayoutState);
    }


    private void loadCategories() {
        showLoadingIndicator();
        CategoriesViewModel categoriesViewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);
        categoriesViewModel.getCategories().observe(this, new Observer<List<Categories>>() {
            @Override
            public void onChanged(@Nullable List<Categories> categories) {
                mCategoriesAdapter = new CategoriesAdapter(MainActivity.this, categories);
                mCategoriesRecyclerView.setAdapter(mCategoriesAdapter);

                mFTProducts.clear();
                for (int i = 0; i < categories.size(); i++) {
                    mFTProducts.addAll(categories.get(i).getCategoryProducts());
                }
                if (mLayoutState != null) {
                    mCategoriesRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutState);
                }
                loadFeatureProducts();
            }
        });

    }

    private void setupViews() {
        if (isNetworkAvailable()) {
            hideRetryButton();
            setupTextSwitcher();
            setupRecyclerViews();
            loadCategories();
        } else {
            showRetryButton();
            hideFeaturedProductsLabel();
            connectionErrorMessage();
        }
    }

    @OnClick(R.id.retryConnectionButton)
    public void onRetryButtonClick() {
        setupViews();
    }

    private void hideRetryButton() {
        mRetryButton.setVisibility(View.GONE);
    }

    private void showRetryButton() {
        mRetryButton.setVisibility(View.VISIBLE);
    }

    private void hideFeaturedProductsLabel() {
        mFTProductsLabel.setVisibility(View.GONE);
    }

    private void showFeaturedProductsLabel() {
        mFTProductsLabel.setVisibility(View.VISIBLE);
    }

    private void connectionErrorMessage() {
        Alerter.create(this)
                .setTitle(R.string.connection_alerter_title)
                .setText(R.string.connection_alerter_text)
                .setIcon(R.drawable.ic_network)
                .setDuration(getResources().getInteger(R.integer.alerter_default_duration))
                .setDismissable(true)
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showLoadingIndicator() {
        hideFeaturedProductsLabel();
        mFTProductsRecyclerView.setVisibility(View.GONE);
        mCategoriesRecyclerView.setVisibility(View.GONE);
        mFTProductPrice.setVisibility(View.GONE);
        mFTProductNameTextSwitcher.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
        mFTProductsRecyclerView.setVisibility(View.VISIBLE);
        mCategoriesRecyclerView.setVisibility(View.VISIBLE);
        mFTProductPrice.setVisibility(View.VISIBLE);
        mFTProductNameTextSwitcher.setVisibility(View.VISIBLE);
        showFeaturedProductsLabel();
    }

    private void setupTextSwitcher() {
        mFTProductNameTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(MainActivity.this);
                textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                textView.setTextColor(getResources().getColor(R.color.primaryTextColor));
                textView.setTextSize(16f);
                Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.poppins);
                textView.setTypeface(typeface);
                return textView;
            }
        });
        mFTProductPrice.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(MainActivity.this);
                textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                textView.setTextColor(getResources().getColor(R.color.primaryTextColor));
                textView.setTextSize(16f);
                Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.poppins);
                textView.setTypeface(typeface);
                return textView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        mFTProductNameTextSwitcher.setInAnimation(in);
        mFTProductNameTextSwitcher.setOutAnimation(out);
        mFTProductPrice.setInAnimation(in);
        mFTProductPrice.setOutAnimation(out);
    }

    private void onActiveCardChange() {
        final int position = mFTLayoutManager.getActiveCardPosition();
        if (position == RecyclerView.NO_POSITION || position == mFTRecyclerViewPosition) {
            return;
        }
        onActiveCardChange(position);
    }

    private void onActiveCardChange(int position) {
        mFTProductNameTextSwitcher.setText(String.valueOf(mFTProducts.get(position).getName()));
        mFTProductPrice.setText("$" + new DecimalFormat().format(mFTProducts.get(position).getPrice()));
        mFTRecyclerViewPosition = position;
    }

    private void setupNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        mToolbar.setTitle(R.string.app_name);
    }

    private void setupHeader() {
        View navigationViewHeader = mNavigationView.getHeaderView(0);
        mProfileHeader = navigationViewHeader.findViewById(R.id.profileHeaderBackground);
        mProfileImage = navigationViewHeader.findViewById(R.id.profileImageView);
        mUserNameTextView = navigationViewHeader.findViewById(R.id.profileNameTextView);
        mUserEmailTextView = navigationViewHeader.findViewById(R.id.profileEmailTextView);
        setupHeaderViews();
    }

    private void setupHeaderViews() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GlideApp.with(this)
                .load(getString(R.string.main_activity_header_image))
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .into(mProfileHeader);

        GlideApp.with(this).load(user.getPhotoUrl())
                .apply(new RequestOptions().override(400, 600))
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .apply(RequestOptions.circleCropTransform())
                .into(mProfileImage);
        mUserNameTextView.setText(String.valueOf(user.getDisplayName()));
        mUserEmailTextView.setText(String.valueOf(user.getEmail()));
    }

    private void setupCartMenu() {
        mCartBadgeTextView = mNavigationView.getMenu().findItem(R.id.cartMenuItemAction).getActionView().findViewById(R.id.menuItemCartBadgeNavDrawerTextView);
        CartViewModel model = ViewModelProviders.of(this).get(CartViewModel.class);
        model.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mCartBadgeTextView.setText(String.valueOf(user.getProductsCount()));
            }
        });

        setupWishlistMenu();
    }

    private void setupWishlistMenu() {
        mWishlistProductsBadgeTextView = mNavigationView.getMenu().findItem(R.id.wishlistMenuItemAction).getActionView().findViewById(R.id.menuItemWishlistedBadgeNavDrawerTextView);
        WishlistProductsViewModel wishlistProductsViewModel = ViewModelProviders.of(this).get(WishlistProductsViewModel.class);
        wishlistProductsViewModel.getWishlistedProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products != null)
                    mWishlistProductsBadgeTextView.setText(String.valueOf(products.size()));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void loadFeatureProducts() {
        ProductImagesAdapter mFeaturedProductsAdapter = new ProductImagesAdapter(mFTProducts, this);
        mFTProductsRecyclerView.setAdapter(mFeaturedProductsAdapter);
        hideLoadingIndicator();
        TransitionManager.beginDelayedTransition(mDrawerLayout,
                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        mFTProductNameTextSwitcher.setText(String.valueOf(mFTProducts.get(mFTRecyclerViewPosition).getName()));
        mFTProductPrice.setText("$" + new DecimalFormat().format(mFTProducts.get(mFTRecyclerViewPosition).getPrice()));

    }

    private void setupRecyclerViews() {
        mCategoriesRecyclerView.setNestedScrollingEnabled(false);
        mFTProductsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });
        mFTLayoutManager = new CardSliderLayoutManager(this);
        mFTProductsRecyclerView.setLayoutManager(mFTLayoutManager);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileMenuItemAction:
                launchUserOrdersActivity();
                break;
            case R.id.cartMenuItemAction:
                launchCartActivity();
                break;
            case R.id.wishlistMenuItemAction:
                launchWishlistActivity();
                break;
            case R.id.signOutMenuItemAction:
                signOutUser();
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchUserOrdersActivity() {
        Intent intent = new Intent(this, UserOrdersActivity.class);
        startActivity(intent);
    }

    private void launchWishlistActivity() {
        Intent intent = new Intent(this, WishlistProductsActivity.class);
        startActivity(intent);
    }

    private void signOutUser() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    private void launchCartActivity() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

}
