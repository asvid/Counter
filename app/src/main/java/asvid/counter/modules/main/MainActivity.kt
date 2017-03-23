package asvid.counter.modules.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.EditText
import asvid.counter.Di
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import asvid.counter.dialogs.DialogCallback
import asvid.counter.modules.counter_details.CounterDetailsActivity
import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder
import kotlinx.android.synthetic.main.activity_main.availableCountersText
import kotlinx.android.synthetic.main.activity_main.counterList
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), CounterListListener {

    private var counterAdapter: CounterListAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        Di.setDialogManager(this@MainActivity)
        Di.analyticsHelper.sendScreenName(this, "MainActivity")
        val name = findViewById(id.name) as EditText
        val value = findViewById(id.value) as EditText
        val addButton = findViewById(id.addButton) as Button

        addButton.setOnClickListener { addItem(name.text.toString(), value.text.toString()) }

        //showColors();
    }

    private fun setList() {
        val itemList = CounterItemManager.getAllCounterItems()
        if (itemList.isEmpty()) availableCountersText.visibility = GONE
        counterAdapter = CounterListAdapter(itemList, this)

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

    override fun onItemEdit(item: CounterItem, position: Int) {
        Di.dialogManager?.showCounterEditDialog(item, object : DialogCallback {
            override fun onPositiveClicked() {
                counterAdapter.notifyItemChanged(position)
            }

            override fun onNegativeClicked() {
            }

        })
    }

    override fun onItemIncrement(item: CounterItem, position: Int) {
        CounterItemManager.incrementAndSave(item)
        counterAdapter.notifyItemChanged(position)
    }

    override fun onItemDecrement(item: CounterItem, position: Int) {
        CounterItemManager.decrementAndSave(item)
        counterAdapter.notifyItemChanged(position)
    }

    override fun onItemClicked(item: CounterItem, position: Int) {
//NOOP
    }

    override fun onDetailsClicked(item: CounterItem, position: Int,
        holder: CounterItemViewHolder) {
        val intent = Intent(this, CounterDetailsActivity::class.java)
        intent.putExtra(CounterDetailsActivity.EXTRA_COUNTER, item.id)
        val p1: Pair<View, String> = Pair.create(
            holder.name, "counterNameTransition")
        val p2: Pair<View, String> = Pair.create(
            holder.changeDate, "counterChangeDateTransition")
        val options = ActivityOptionsCompat.
            makeSceneTransitionAnimation(this, p1, p2)
        startActivity(intent, options.toBundle())
//        startActivity(Intent(this, Test::class.java))
    }

    override fun onResume() {
        super.onResume()
        setList()
    }
}
