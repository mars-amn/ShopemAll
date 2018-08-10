package elamien.abdullah.shopemall.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import elamien.abdullah.shopemall.model.Product;


@Database(entities = {Product.class}, version = 3, exportSchema = false)
@TypeConverters(DateConverters.class)
public abstract class WishlistDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "WishlistProducts";
    private static final Object LOCK = new Object();

    private static WishlistDatabase sDatabase;

    public static WishlistDatabase getDatabase(Context context) {
        if (sDatabase == null) {
            synchronized (LOCK) {
                sDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        WishlistDatabase.class,
                        DATABASE_NAME)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sDatabase;
    }

    public abstract WishListDao mWishListProductsDao();
}
