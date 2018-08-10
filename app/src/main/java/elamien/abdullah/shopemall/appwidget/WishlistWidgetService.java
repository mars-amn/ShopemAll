package elamien.abdullah.shopemall.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import elamien.abdullah.shopemall.R;

/**
 * Created by AbdullahAtta on 7/12/2018.
 */
public class WishlistWidgetService extends JobIntentService {

    private static final int JOB_ID = 1;
    private static final String UPDATE_WIDGET_ACTION = "elamien.abdullah.shopemall.appwidget.update_wishlist_widget";

    static void enqueueService(Context context) {
        Intent intent = new Intent(context, WishlistWidgetService.class);
        intent.setAction(UPDATE_WIDGET_ACTION);
        enqueueWork(context, WishlistWidgetService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String action = intent.getAction();

        if (UPDATE_WIDGET_ACTION.equals(action)) {
            updateWishlistWidget();
        }

    }

    private void updateWishlistWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WishlistAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.wishlistWidgetGridView);
        WishlistAppWidget.updateWishlistAppWidget(this, appWidgetManager, appWidgetIds);
    }
}
