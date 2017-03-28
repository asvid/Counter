package asvid.counter.modules.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.EditText
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.data.CounterItem
import asvid.counter.data.CounterItemManager
import asvid.counter.dialogs.DialogCallback
import asvid.counter.dialogs.DialogManager
import asvid.counter.modules.app_info.AppInfoActivity
import asvid.counter.modules.counter_details.CounterDetailsActivity
import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder
import asvid.counter.modules.widget_list.WidgetListActivity
import kotlinx.android.synthetic.main.activity_main.availableCountersText
import kotlinx.android.synthetic.main.activity_main.counterList
import timber.log.Timber
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), CounterListListener {

    private var counterAdapter: CounterListAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        Di.analyticsHelper.sendScreenName(this, "MainActivity")
        val name = findViewById(id.name) as EditText
        val value = findViewById(id.value) as EditText
        val addButton = findViewById(id.addButton) as Button

        addButton.setOnClickListener { addItem(name.text.toString(), value.text.toString()) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        Timber.d("item id: $id")
        when (id) {
            R.id.action_app_info -> openAppInfo()
            R.id.action_widget_list -> openWidgetList()
        }
        return true
    }

    private fun openWidgetList() {
        startActivity(Intent(this, WidgetListActivity::class.java))
    }

    private fun openAppInfo() {
        startActivity(Intent(this, AppInfoActivity::class.java))
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
        DialogManager.showCounterDeleteDialog(this, item, object : DialogCallback {
            override fun onPositiveClicked() {
                CounterItemManager.deleteCounterItem(item)
                counterAdapter.removeItem(position)
            }

            override fun onNegativeClicked() {

            }
        })
    }

    override fun onItemEdit(item: CounterItem, position: Int) {
        DialogManager.showCounterEditDialog(this, item, object : DialogCallback {
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
