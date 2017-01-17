package widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import asvid.beercounter.R;
import asvid.beercounter.data.CounterItem;
import asvid.beercounter.data.Storage;

/**
 * Created by adam on 14.01.17.
 */

public class BeerCounterWidgetProvider extends AppWidgetProvider {

    public static final String CLICKED = "CLICKED";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        Log.d("WIDGET", "onUpdate ");
        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                BeerCounterWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            // create some random data

            Log.d("WIDGET", "widgetId " + widgetId);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.beer_counter_appwidget);

            // Register an onClickListener
            setOnClick(context, widgetId, remoteViews);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private static void setOnClick(Context context, int widgetId,
                                   RemoteViews remoteViews) {
        Intent intent = new Intent(context, BeerCounterWidgetProvider.class);

        intent.setAction(CLICKED);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, widgetId, intent, 0);
        remoteViews
                .setOnClickPendingIntent(R.id.beerCounterView, pendingIntent);
    }

    public static void updateAppWidget(Context context,
                                       AppWidgetManager appWidgetManager,
                                       int mAppWidgetId, CounterItem item) {
        final RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.beer_counter_appwidget);
        views.setTextViewText(R.id.name, String.valueOf(item.getName()));
        views.setTextViewText(R.id.value, String.valueOf(item.getValue()));
        setOnClick(context, mAppWidgetId, views);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(CLICKED)) {
            int widgetId = intent
                    .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);

            Storage storage = new Storage(context);
            CounterWidget widget = storage.getWidget(widgetId);

            CounterItem item = widget.getCounterItem();
            storage.increaseAndSave(item);

            updateAppWidget(context, appWidgetManager, widgetId, item);

            Log.d("WIDGET", "onReceive " + widgetId);
        }
    }
}
