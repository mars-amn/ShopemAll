package elamien.abdullah.shopemall.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.fmsirvent.ParallaxEverywhere.PEWImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.GlideApp;
import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.activities.CategoryProductsActivity;
import elamien.abdullah.shopemall.model.Categories;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewsHolder> {

    private Context mContext;
    private List<Categories> mCategoriesList;
    private RequestOptions mRequestOptions;

    public CategoriesAdapter(Context mContext, List<Categories> categories) {
        this.mContext = mContext;
        this.mCategoriesList = categories;
        mRequestOptions = new RequestOptions();
    }

    @NonNull
    @Override
    public CategoriesViewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoriesView = LayoutInflater.from(mContext).inflate(R.layout.list_item_categories, parent, false);
        return new CategoriesViewsHolder(categoriesView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewsHolder holder, int position) {
        String categoryImageUrl = mCategoriesList.get(position).getCategoryImage();
        String categoryName = mCategoriesList.get(position).getCategoryName();
        holder.mCategoryTextView.setText(categoryName);
        GlideApp.with(mContext)
                .load(categoryImageUrl)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .apply(mRequestOptions.override(mContext.getResources().getInteger(R.integer.category_image_width),
                        mContext.getResources().getInteger(R.integer.category_image_height)))
                .into(holder.mCategoryImageView);
    }

    @Override
    public int getItemCount() {
        return mCategoriesList == null ? 0 : mCategoriesList.size();
    }


    public class CategoriesViewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categoryImageView)
        PEWImageView mCategoryImageView;
        @BindView(R.id.categoryNameTextView)
        TextView mCategoryTextView;

        public CategoriesViewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.categoriesCardView)
        public void onCategoryClick() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                launchSceneTransitions();
            } else {
                launchProductsActivity();
            }
        }

        private void launchProductsActivity() {
            Intent intent = new Intent(mContext, CategoryProductsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, mCategoriesList.get(getAdapterPosition()).getCategoryName());
            mContext.startActivity(intent);
        }

        @SuppressLint("NewApi")
        private void launchSceneTransitions() {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle();
            Intent intent = new Intent(mContext, CategoryProductsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, mCategoriesList.get(getAdapterPosition()).getCategoryName());
            mContext.startActivity(intent, bundle);
        }
    }
}
