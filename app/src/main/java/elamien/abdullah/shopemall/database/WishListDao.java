package elamien.abdullah.shopemall.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import elamien.abdullah.shopemall.model.Product;


@Dao
public interface WishListDao {
    @Query("SELECT * FROM WishList ORDER BY createdAt")
    LiveData<List<Product>> getAllProducts();

    @Insert
    void addProductToWishlist(Product product);

    @Query("SELECT * FROM WishList WHERE code =:productCode")
    LiveData<Product> getProduct(int productCode);

    @Query("SELECT * FROM WishList WHERE code =:code")
    Product getProductForExistence(int code);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT * FROM WishList ORDER BY createdAt")
    List<Product> getAllProductsWidget();
}

