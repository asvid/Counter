package widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import asvid.beercounter.App;
import asvid.beercounter.R;
import asvid.beercounter.data.CounterItem;

/**
 * Created by adam on 15.01.17.
 */

public class BeerCounterWidgetConfigurationActivity extends Activity {

    private List<Long> mRowIDs;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private BaseAdapter mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.beer_counter_widget_configuration_activity);

        ListView counterList = (ListView) findViewById(R.id.counterList);
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText value = (EditText) findViewById(R.id.value);
        Button addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(name.getText().toString(), value.getText().toString());
            }
        });

        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        List<CounterItem> itemList = ((App) getApplication()).getStorage()
                .getAllItems();
        mAdapter = new CounterListAdapter(getApplicationContext(),
                R.layout.counter_item_view, itemList);
        counterList.setAdapter(mAdapter);
    }

    private void addItem(String name, String value) {
        CounterItem counterItem = new CounterItem();
        counterItem.setName(name);
        counterItem.setValue(Double.parseDouble(value));
        ((App) getApplication()).getStorage().saveItem(counterItem);

        CounterWidget widget = new CounterWidget();
        widget.setCounterItem(counterItem);
        widget.setId(mAppWidgetId);
        ((App) getApplication()).getStorage().saveWidget(widget);

        // Request widget update
        final AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(this);
        BeerCounterWidgetProvider
                .updateAppWidget(this, appWidgetManager, mAppWidgetId,
                        counterItem);

        setResult(RESULT_OK);
        finish();
    }
}
