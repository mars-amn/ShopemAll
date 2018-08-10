package elamien.abdullah.shopemall.appwidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.activities.WishlistProductDetailsActivity;
import elamien.abdullah.shopemall.database.WishlistDatabase;
import elamien.abdullah.shopemall.model.Product;


public class WishlistGridRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridViewWidgetRemoteViewServiceFactory(this.getApplicationContext());
    }

    class GridViewWidgetRemoteViewServiceFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        private WishlistDatabase mDatabase;
        private List<Product> mWishlistedProducts;

        public GridViewWidgetRemoteViewServiceFactory(Context applicationContext) {
            mContext = applicationContext;
            mDatabase = WishlistDatabase.getDatabase(applicationContext);
            mWishlistedProducts = mDatabase.mWishListProductsDao().getAllProductsWidget();
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            mWishlistedProducts = mDatabase.mWishListProductsDao().getAllProductsWidget();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mWishlistedProducts == null ? 0 : mWishlistedProducts.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (mWishlistedProducts.size() == 0) {
                return null;
            }

            Product product = mWishlistedProducts.get(i);
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_app_wishlist_list_item);
            String image = product.getImage();
            try {
                Bitmap bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load(image)
                        .submit()
                        .get();

                remoteViews.setImageViewBitmap(R.id.wishlistProductWidgetImage, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            remoteViews.setTextViewText(R.id.wishlistProductWidgetNameTextView, product.getName());
            remoteViews.setTextViewText(R.id.wishlistProductWidgetPriceTextView, "$" + new DecimalFormat().format(product.getPrice()));

            Bundle extras = new Bundle();
            extras.putInt(Intent.EXTRA_TEXT, product.getCode());
            Intent fillIntent = new Intent(mContext, WishlistProductDetailsActivity.class);
            fillIntent.putExtras(extras);
            remoteViews.setOnClickFillInIntent(R.id.wishlistProductWidgetImage, fillIntent);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
