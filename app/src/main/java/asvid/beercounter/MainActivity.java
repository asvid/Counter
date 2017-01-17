package asvid.beercounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import asvid.beercounter.data.CounterItem;
import widget.CounterListAdapter;

public class MainActivity extends AppCompatActivity {

    private CounterListAdapter mAdapter;
    private ListView counterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        counterList = (ListView) findViewById(R.id.counterList);

        List<CounterItem> itemList = ((App) getApplication()).getStorage()
                .getAllItems();
        mAdapter = new CounterListAdapter(getApplicationContext(),
                R.layout.counter_item_view, itemList);

        Log.d("ITEMS", "items: " + itemList);
        counterList.setAdapter(mAdapter);

        final EditText name = (EditText) findViewById(R.id.name);
        final EditText value = (EditText) findViewById(R.id.value);
        Button addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(name.getText().toString(), value.getText().toString());
            }
        });
    }

    private void addItem(String name, String value) {
        CounterItem counterItem = new CounterItem();
        counterItem.setName(name);
        counterItem.setValue(Double.parseDouble(value));
        ((App) getApplication()).getStorage().saveItem(counterItem);
        List<CounterItem> itemList = ((App) getApplication()).getStorage()
                .getAllItems();
        mAdapter = new CounterListAdapter(getApplicationContext(),
                R.layout.counter_item_view, itemList);

        Log.d("ITEMS", "items: " + itemList);
        counterList.setAdapter(mAdapter);
    }
}
