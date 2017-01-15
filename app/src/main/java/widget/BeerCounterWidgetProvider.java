package widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import asvid.beercounter.R;
import asvid.beercounter.data.CounterItem;

/**
 * Created by adam on 14.01.17.
 */

public class BeerCounterWidgetProvider extends AppWidgetProvider {

    public static final String CLICKED = "CLICKED";
    public static final String ACTION = "ACTION";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                BeerCounterWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            // create some random data

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.beer_counter_appwidget);

            // Register an onClickListener
            Intent intent = new Intent(context,
                    BeerCounterWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.putExtra(ACTION, CLICKED);

            PendingIntent pendingIntent = PendingIntent
                    .getBroadcast(context, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.beerCounterView,
                    pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    public static void updateAppWidget(Context context,
                                       AppWidgetManager appWidgetManager,
                                       int mAppWidgetId, CounterItem item) {
        final RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.beer_counter_appwidget);
        views.setTextViewText(R.id.name, String.valueOf(item.getName()));
        views.setTextViewText(R.id.value, String.valueOf(item.getValue()));
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
    }
}
