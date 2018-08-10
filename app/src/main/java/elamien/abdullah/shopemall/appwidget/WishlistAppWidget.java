package elamien.abdullah.shopemall.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import elamien.abdullah.shopemall.R;
import elamien.abdullah.shopemall.activities.WishlistProductDetailsActivity;


public class WishlistAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews gridRemoteViews = getGridWidgetRemoteViews(context);

        appWidgetManager.updateAppWidget(appWidgetId, gridRemoteViews);
    }

    private static RemoteViews getGridWidgetRemoteViews(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_app_wishlist);
        Intent gridViewService = new Intent(context, WishlistGridRemoteService.class);
        remoteViews.setRemoteAdapter(R.id.wishlistWidgetGridView, gridViewService);


        Intent appIntent = new Intent(context, WishlistProductDetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.wishlistWidgetGridView, pendingIntent);
        return remoteViews;
    }

    public static void updateWishlistAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId :
                appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WishlistWidgetService.enqueueService(context);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}


