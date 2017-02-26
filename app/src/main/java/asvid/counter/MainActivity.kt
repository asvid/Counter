package asvid.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View.GONE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), CounterListListener {

    private var counterAdapter: CounterListAdapter by Delegates.notNull()
    private var counterList: RecyclerView by Delegates.notNull()
    private var availableCountersText: TextView by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Di.analyticsHelper.sendScreenName(this, "MainActivity")
        val name = findViewById(R.id.name) as EditText
        val value = findViewById(R.id.value) as EditText
        val addButton = findViewById(R.id.addButton) as Button
        availableCountersText = findViewById(R.id.availableCountersText) as TextView

        addButton.setOnClickListener { addItem(name.text.toString(), value.text.toString()) }

        //showColors();
    }

    private fun setList() {
        val itemList = CounterItemManager.getAllCounterItems()
        if (itemList.isEmpty()) availableCountersText.visibility = GONE
        counterAdapter = CounterListAdapter(itemList, this)

        counterList = findViewById(R.id.counterList) as RecyclerView
        counterList.adapter = counterAdapter
        counterList.layoutManager = LinearLayoutManager(this)
    }

    private fun addItem(name: String, value: String) {
        val counterItem = CounterItem()
        counterItem.name = name
        if (!TextUtils.isEmpty(value)) counterItem.value = Integer.parseInt(value)
        CounterItemManager.saveCounterItem(counterItem)
        counterAdapter.addItem(counterItem)
        counterList.adapter = counterAdapter
    }

    override fun onItemDelete(item: CounterItem, position: Int) {
        CounterItemManager.deleteCounterItem(item)
        counterAdapter.removeItem(position)
    }

    override fun onItemClicked(item: CounterItem, position: Int) {
//        TODO("show edit dialog")
    }

    override fun onItemIncrement(item: CounterItem, position: Int) {
        CounterItemManager.incrementAndSave(item)
        counterAdapter.notifyItemChanged(position)
    }

    override fun onItemDecrement(item: CounterItem, position: Int) {
        CounterItemManager.decrementAndSave(item)
        counterAdapter.notifyItemChanged(position)
    }

    override fun onResume() {
        super.onResume()
        setList()
    }
}
