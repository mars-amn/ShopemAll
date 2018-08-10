package elamien.abdullah.shopemall.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lid.lib.LabelImageView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.GlideApp;
import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.activities.WishlistProductDetailsActivity;
import elamien.abdullah.shopemall.model.Product;


public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistProductsViewHolder> {

    private List<Product> mWishlistedProducts;
    private Context mContext;

    public WishlistAdapter(List<Product> mWishlistedProducts, Context mContext) {
        this.mWishlistedProducts = mWishlistedProducts;
        this.mContext = mContext;
    }

    public List<Product> getProducts() {
        return mWishlistedProducts;
    }

    @NonNull
    @Override
    public WishlistProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View wishlistProductsView = LayoutInflater.from(mContext).inflate(R.layout.list_item_wishlist_products, parent, false);
        return new WishlistProductsViewHolder(wishlistProductsView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WishlistProductsViewHolder holder, int position) {
        Product product = mWishlistedProducts.get(position);

        float productPrice = product.getPrice();
        float productSale = product.getSale();

        if (productSale > 0) {
            setPriceWithSale(holder, productPrice, productSale);
        } else {
            setPriceWithoutSale(holder, productPrice);
        }

        showProductDetails(holder, product);
    }

    private void showProductDetails(@NonNull WishlistProductsViewHolder holder, Product product) {
        GlideApp.with(mContext)
                .load(product.getImage())
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .into(holder.mProductImage);

        holder.mWishlistProductName.setText(product.getName());
        holder.mWishlistProductDescription.setText(product.getDescription());
    }

    @SuppressLint("SetTextI18n")
    private void setPriceWithoutSale(@NonNull WishlistProductsViewHolder holder, float productPrice) {
        holder.mProductImage.setLabelVisual(false);
        holder.mProductPriceTextView.setText("$" + new DecimalFormat().format(productPrice));
    }

    @SuppressLint("SetTextI18n")
    private void setPriceWithSale(@NonNull WishlistProductsViewHolder holder, float productPrice, float productSale) {
        float saleInPercentage = (productSale / productPrice) * 100;
        holder.mProductImage.setLabelText(new DecimalFormat("#.#").format(saleInPercentage) + mContext.getString(R.string.percent_sale_off_label));
        holder.mProductPriceTextView.setText("$" + new DecimalFormat().format(productPrice - productSale));
    }

    @Override
    public int getItemCount() {
        return mWishlistedProducts == null ? 0 : mWishlistedProducts.size();
    }

    public class WishlistProductsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.wishlistedProductImage)
        LabelImageView mProductImage;
        @BindView(R.id.wishlistedProductDescriptionTextView)
        TextView mWishlistProductDescription;
        @BindView(R.id.wishlistedProductNameTextView)
        TextView mWishlistProductName;
        @BindView(R.id.wishlistedProductPriceTextView)
        TextView mProductPriceTextView;

        public WishlistProductsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.wishlistedProductListParent)
        public void onWishlistedProductClick() {
            Intent intent = new Intent(mContext, WishlistProductDetailsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, mWishlistedProducts.get(getAdapterPosition()).getCode());
            mContext.startActivity(intent);
        }
    }
}