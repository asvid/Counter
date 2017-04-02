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
import asvid.counter.Di
import asvid.counter.R
import asvid.counter.R.id
import asvid.counter.R.layout
import asvid.counter.data.counter.CounterItem
import asvid.counter.data.counter.CounterItemManager
import asvid.counter.dialogs.DialogCallback
import asvid.counter.dialogs.DialogManager
import asvid.counter.modules.app_info.AppInfoActivity
import asvid.counter.modules.counter_details.CounterDetailsActivity
import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder
import asvid.counter.modules.widget_list.WidgetListActivity
import kotlinx.android.synthetic.main.activity_main.availableCountersText
import kotlinx.android.synthetic.main.activity_main.counterList
import kotlinx.android.synthetic.main.activity_main.counterName
import kotlinx.android.synthetic.main.activity_main.counterNameInputLayer
import kotlinx.android.synthetic.main.activity_main.counterStartValue
import timber.log.Timber
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var counterAdapter: CounterListAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        Di.analyticsHelper.sendScreenName(this, "MainActivity")
        val addButton = findViewById(id.addButton) as Button

        addButton.setOnClickListener {
            if (!TextUtils.isEmpty(counterName.text))
                addItem(counterName.text.toString(), counterStartValue.text.toString())
            else {
                counterNameInputLayer.error = resources.getString(R.string.no_name_error)
            }
        }

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
        counterAdapter = CounterListAdapter(itemList)

        counterList.adapter = counterAdapter
        counterList.layoutManager = LinearLayoutManager(this)

        counterAdapter.getPositionClicks().subscribe {
            action ->
            when (action.action) {
                ACTION.DELETE -> onItemDelete(action.item, action.position)
                ACTION.ITEM_CLICKED -> onItemClicked(action.item, action.position)
                ACTION.EDIT -> onItemEdit(action.item, action.position)
                ACTION.DETAILS -> onDetailsClicked(action.item, action.position, action.holder)
                ACTION.INCREMENT -> onItemIncrement(action.item, action.position)
                ACTION.DECREMENT -> onItemDecrement(action.item, action.position)
            }
        }
    }

    private fun addItem(name: String, value: String) {
        val counterItem = CounterItem()
        counterItem.name = name
        if (!TextUtils.isEmpty(value)) counterItem.value = Integer.parseInt(value)
        CounterItemManager.saveCounterItem(counterItem)
        counterAdapter.addItem(counterItem)
        counterList.adapter = counterAdapter
        counterName.text.clear()
    }

    //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
    fun onItemDelete(item: CounterItem, position: Int) {
        DialogManager.showCounterDeleteDialog(this, item, object : DialogCallback {
            override fun onPositiveClicked() {
                CounterItemManager.deleteCounterItem(item)
                counterAdapter.removeItem(position)
            }

            override fun onNegativeClicked() {

            }
        })
    }

    //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
    fun onItemEdit(item: CounterItem, position: Int) {
        DialogManager.showCounterEditDialog(this, item, object : DialogCallback {
            override fun onPositiveClicked() {
                counterAdapter.notifyItemChanged(position)
            }

            override fun onNegativeClicked() {
            }

        })
    }

    //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
    fun onItemIncrement(item: CounterItem, position: Int) {
        CounterItemManager.incrementAndSave(item)
        counterAdapter.notifyItemChanged(position)
    }

    //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
    fun onItemDecrement(item: CounterItem, position: Int) {
        CounterItemManager.decrementAndSave(item)
        counterAdapter.notifyItemChanged(position)
    }

    //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
    fun onItemClicked(item: CounterItem, position: Int) {
//NOOP
    }

    //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
    fun onDetailsClicked(item: CounterItem, position: Int,
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
    }

    override fun onResume() {
        super.onResume()
        setList()
    }
}
