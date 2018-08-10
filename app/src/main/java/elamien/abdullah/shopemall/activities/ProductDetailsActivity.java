package elamien.abdullah.shopemall.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.GlideApp;
import elamien.abdullah.shopemall.LabelKenBurnsView;
import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.database.WishlistDatabase;
import elamien.abdullah.shopemall.firebaseviewmodel.CartViewModel;
import elamien.abdullah.shopemall.firebaseviewmodel.ProductsViewModel;
import elamien.abdullah.shopemall.firebaseviewmodel.ProductsViewModelFactory;
import elamien.abdullah.shopemall.model.Cart;
import elamien.abdullah.shopemall.model.Product;
import elamien.abdullah.shopemall.model.User;
import elamien.abdullah.shopemall.utils.Constants;

public class ProductDetailsActivity extends AppCompatActivity {

    @BindView(R.id.detailProductImage)
    LabelKenBurnsView mProductImage;
    @BindView(R.id.detailProductDescriptionTextView)
    TextView mProductDescriptionTextView;
    @BindView(R.id.detailProductNameTextView)
    TextView mProductNameTextView;
    @BindView(R.id.detailProductTotalPrice)
    TextView mPriceTextView;
    @BindView(R.id.detailProductDiscountPrice)
    TextView mDiscountPriceTextView;
    @BindView(R.id.productCategoryHashtagTextView)
    TextView mCategoryHashTag;
    @BindView(R.id.productFABMenu)
    FloatingActionMenu mFABsMenu;
    @BindView(R.id.addToCartFAB)
    FloatingActionButton mAddToCartFab;
    @BindView(R.id.addToWishListFAB)
    FloatingActionButton mAddToWishListFab;
    @BindView(R.id.shareProductFAB)
    FloatingActionButton mShareFAB;
    WishlistDatabase mWishlistDatabase;
    private TextView mCartBadgeTextView;
    private String mProductCategory;
    private int mProductCode;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT) && intent.hasExtra(Intent.EXTRA_TITLE)) {
            mProductCategory = intent.getStringExtra(Intent.EXTRA_TITLE);
            mProductCode = intent.getIntExtra(Intent.EXTRA_TEXT, -1);
            setupViewModel();
        } else {
            errorUponLaunch();
        }
    }

    @SuppressLint("RestrictedApi")
    private void setupFABs() {
        mAddToCartFab.setImageDrawable(AppCompatDrawableManager.get().getDrawable(this, R.drawable.ic_shopping_cart));
        setupAddToWishListFAB();
        mShareFAB.setImageDrawable(AppCompatDrawableManager.get().getDrawable(this, R.drawable.ic_share));
    }

    private void setupAddToWishListFAB() {
        if (isProductNotWishlisted()) {
            setWishListFABToAdd();
        } else {
            setWishListFABToRemove();
        }

    }

    @SuppressLint("RestrictedApi")
    private void setWishListFABToRemove() {
        mAddToWishListFab.setLabelText(getString(R.string.remove_product_wishlist_fab_label));
        mAddToWishListFab.setImageDrawable(AppCompatDrawableManager.get().getDrawable(this, R.drawable.ic_wishlist_remove));
    }

    @SuppressLint("RestrictedApi")
    private void setWishListFABToAdd() {
        mAddToWishListFab.setLabelText(getString(R.string.add_product_wishlist_fab_label));
        mAddToWishListFab.setImageDrawable(AppCompatDrawableManager.get().getDrawable(this, R.drawable.ic_add_wishlist));
    }

    @OnClick(R.id.addToWishListFAB)
    public void onAddToWishListClick() {
        if (isProductNotWishlisted()) {
            addProductToWishList();
            setWishListFABToRemove();
        } else {
            deleteProductFromDatabase();
        }
    }

    private void deleteProductFromDatabase() {
        WishlistDatabase database = WishlistDatabase.getDatabase(this);
        database.mWishListProductsDao().deleteProduct(mProduct);
        showDeleteAlerterMsg();
        setWishListFABToAdd();
        mFABsMenu.close(true);
    }

    private void showDeleteAlerterMsg() {
        Alerter.create(this)
                .setText(R.string.product_removed_wishlist_msg)
                .setDuration(1000)
                .setIcon(R.drawable.ic_wishlist_remove)
                .setDismissable(true)
                .show();
    }

    private void addProductToWishList() {
        Product product = new Product(mProduct);
        mWishlistDatabase.mWishListProductsDao().addProductToWishlist(product);
        mFABsMenu.close(true);
    }

    private boolean isProductNotWishlisted() {
        Product productListed = mWishlistDatabase.mWishListProductsDao().getProductForExistence(mProduct.getCode());
        return productListed == null;
    }

    @OnClick(R.id.shareProductFAB)
    public void onShareFABClick() {
        String text;
        if (mProduct.getSale() > 0) {
            text = shareTextWithSale();
        } else {
            text = shareTextWithoutSale();
        }
        shareProduct(text);
    }

    @NonNull
    private String shareTextWithoutSale() {
        return mProduct.getName() + " " + getString(R.string.share_product_sale_for_part) + "$" + new DecimalFormat().format(mProduct.getPrice()) + "\n\n\n" +
                mProduct.getDescription();
    }

    @NonNull
    private String shareTextWithSale() {
        return getString(R.string.share_product_sale_first_part) + mProduct.getName() + getString(R.string.share_product_sale_for_part) +
                "$" + new DecimalFormat().format(mProduct.getPrice() - mProduct.getSale()) +
                getString(R.string.share_product_sale_second_part) + "$" + new DecimalFormat().format(mProduct.getPrice()) + getString(R.string.share_product_sale_on_part) + getString(R.string.app_name) +
                " " + mProduct.getName() + "\n\n\n" + mProduct.getDescription();
    }

    private void shareProduct(String text) {
        mFABsMenu.close(true);
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.share_product_chooser_title)
                .setText(text)
                .startChooser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_activity_menu, menu);
        final MenuItem cartMenuItem = menu.findItem(R.id.cartMenuItemAction);
        View actionView = cartMenuItem.getActionView();
        mCartBadgeTextView = actionView.findViewById(R.id.cartMenuItemBadge);

        setupCartBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(cartMenuItem);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setupCartBadge() {
        CartViewModel model = ViewModelProviders.of(this).get(CartViewModel.class);
        model.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mCartBadgeTextView.setText(String.valueOf(user.getProductsCount()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cartMenuItemAction:
                launchCartActivity();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.addToCartFAB)
    public void addProductToUserCart() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference().child(Constants.USER_CHILD);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Product product = mProduct;
        final int productCode = product.getCode();

        reference.orderByChild(Constants.USER_ID_CHILD).equalTo(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    createNewCart(product, firebaseUser, reference);
                } else {
                    reference.orderByChild(Constants.USER_ID_CHILD).equalTo(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String key = dataSnapshot.getKey();
                            User user = dataSnapshot.getValue(User.class);
                            Cart cart = user.getCart();
                            List<Product> productList = cart.getCartProducts();
                            List<Integer> codes = new ArrayList<>();

                            for (int i = 0; i < productList.size(); i++) {
                                codes.add(productList.get(i).getCode());
                            }

                            for (int i = 0; i < productList.size(); i++) {
                                Product product1 = productList.get(i);
                                if (product1.getCode() == productCode) {
                                    product1.setShoppingCartQuantity(product1.getShoppingCartQuantity() + 1);
                                    productList.set(i, product1);
                                    cart.setCartProducts(productList);
                                    user.setCart(cart);
                                    user.setProductsCount(productList.size());
                                    reference.child(key).updateChildren(user.toMap());
                                    showProductCartQuantityAlerterMsg();
                                    break;
                                }

                            }

                            if (!codes.contains(productCode)) {
                                product.setShoppingCartQuantity(1);
                                productList.add(product);
                                cart.setCartProducts(productList);
                                user.setCart(cart);
                                user.setProductsCount(productList.size());
                                reference.child(key).setValue(user);
                                showCartAddedAlerterMsg();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createNewCart(Product product, FirebaseUser firebaseUser, DatabaseReference reference) {
        List<Product> products = new ArrayList<>();
        product.setShoppingCartQuantity(1);
        products.add(product);
        Cart cart = new Cart(products, firebaseUser.getUid());
        User user1 = new User(firebaseUser.getDisplayName(), firebaseUser.getUid(), cart, products.size());
        reference.push().setValue(user1);
        showCartAddedAlerterMsg();
    }

    private void showProductCartQuantityAlerterMsg() {
        mFABsMenu.close(true);
        Alerter.create(this)
                .setTitle(R.string.cart_add_product_exists_alerter_msg)
                .setText(R.string.cart_add_product_increase_quantity_alerter_text)
                .setDuration(getResources().getInteger(R.integer.alerter_default_duration))
                .setIcon(R.drawable.ic_plus_one)
                .setDismissable(true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchCartActivity();
                    }
                })
                .show();
    }

    private void showCartAddedAlerterMsg() {
        mFABsMenu.close(true);
        Alerter.create(this)
                .setText(R.string.cart_add_product_alerter_msg)
                .setIcon(R.drawable.ic_shopping_cart)
                .setDismissable(true)
                .setDuration(getResources().getInteger(R.integer.alerter_default_duration))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchCartActivity();
                    }
                })
                .show();
    }

    private void launchCartActivity() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }


    private void setupViewModel() {
        ProductsViewModelFactory factory = new ProductsViewModelFactory(mProductCode, mProductCategory);
        ProductsViewModel model = ViewModelProviders.of(this, factory).get(ProductsViewModel.class);
        model.getSpecificProduct().observe(this, new Observer<Product>() {
            @Override
            public void onChanged(@Nullable Product product) {
                showProductDetails(product);
            }
        });
    }

    private void showProductDetails(Product product) {
        mWishlistDatabase = WishlistDatabase.getDatabase(this);
        mProduct = product;
        setupFABs();

        float productSale = product.getSale();
        float productPrice = product.getPrice();
        if (productSale > 0) {
            setPriceWithSale(productSale, productPrice);
        } else {
            setPriceWithoutSale(productPrice);
        }
        setProductDetails(product);
        mCategoryHashTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setProductDetails(Product product) {
        GlideApp.with(this)
                .load(product.getImage())
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .into(mProductImage);
        mProductDescriptionTextView.setText(product.getDescription());
        mProductNameTextView.setText(product.getName());
        mCategoryHashTag.setText(String.valueOf("#" + product.getCategory()));
    }

    private void setPriceWithoutSale(float productPrice) {
        mProductImage.setLabelVisual(false);
        mDiscountPriceTextView.setVisibility(View.GONE);
        mPriceTextView.setText(new DecimalFormat().format(productPrice));
    }

    @SuppressLint("SetTextI18n")
    private void setPriceWithSale(float productSale, float productPrice) {
        float saleInPercentage = (productSale / productPrice) * 100;
        String saleText = new DecimalFormat("#.#").format(saleInPercentage);
        mProductImage.setLabelText(saleText + getString(R.string.percent_sale_off_label));
        mDiscountPriceTextView.setText("$" + new DecimalFormat().format(productPrice - productSale));
        mPriceTextView.setText("$" + new DecimalFormat().format(productPrice));
        mPriceTextView.setPaintFlags(mPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void errorUponLaunch() {
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        finish();
    }
}
