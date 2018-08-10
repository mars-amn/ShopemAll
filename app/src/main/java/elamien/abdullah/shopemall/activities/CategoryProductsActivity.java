package elamien.abdullah.shopemall.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.adapters.ProductsAdapter;
import elamien.abdullah.shopemall.firebaseviewmodel.CartViewModel;
import elamien.abdullah.shopemall.firebaseviewmodel.ProductsViewModel;
import elamien.abdullah.shopemall.firebaseviewmodel.ProductsViewModelFactory;
import elamien.abdullah.shopemall.model.Product;
import elamien.abdullah.shopemall.model.User;

public class CategoryProductsActivity extends AppCompatActivity {

    @BindView(R.id.productsRecyclerView)
    RecyclerView mProductsRecyclerView;
    @BindView(R.id.categoryProductsToolbar)
    Toolbar mToolbar;
    @BindView(R.id.productsSearchView)
    MaterialSearchView mSearchView;

    private TextView mCartBadgeTextView;
    private String mCategory;
    private ProductsAdapter mProductsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_category_products);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Intent intent = getIntent();
        setupSearchView();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mCategory = intent.getStringExtra(Intent.EXTRA_TEXT);
            setupToolbar();
            loadCategoryProducts();

        } else {
            errorUponLaunch();
        }


    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mCategory);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void loadCategoryProducts() {
        ProductsViewModelFactory factory = new ProductsViewModelFactory(mCategory);
        ProductsViewModel productsViewModel = ViewModelProviders.of(this, factory).get(ProductsViewModel.class);
        productsViewModel.getCategoryProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                mProductsAdapter = new ProductsAdapter(CategoryProductsActivity.this, products);
                mProductsRecyclerView.setAdapter(mProductsAdapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void setupSearchView() {
        mSearchView.setVoiceSearch(true);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(CategoryProductsActivity.this, SearchableActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, query);
                intent.putExtra(Intent.EXTRA_TITLE, mCategory);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_products_menu, menu);
        final MenuItem cartMenuItem = menu.findItem(R.id.cartMenuItemAction);
        MenuItem searchItem = menu.findItem(R.id.searchAction);
        mSearchView.setMenuItem(searchItem);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cartMenuItemAction:
                launchCartActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchCartActivity() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
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


    private void errorUponLaunch() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
        finish();
    }
}
