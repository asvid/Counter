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
import asvid.counter.R
import asvid.counter.R.layout
import asvid.counter.di.Di
import asvid.counter.dialogs.DialogCallback
import asvid.counter.dialogs.DialogManager
import asvid.counter.model.Counter
import asvid.counter.modules.app_info.AppInfoActivity
import asvid.counter.modules.counter_details.CounterDetailsActivity
import asvid.counter.modules.main.CounterListAdapter.CounterItemViewHolder
import asvid.counter.modules.widget_list.WidgetListActivity
import asvid.counter.repository.CounterRepository
import asvid.counter.utils.startAlphaAnimation
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.addButton
import kotlinx.android.synthetic.main.activity_main.availableCountersText
import kotlinx.android.synthetic.main.activity_main.counterList
import kotlinx.android.synthetic.main.activity_main.counterName
import kotlinx.android.synthetic.main.activity_main.counterNameInputLayer
import kotlinx.android.synthetic.main.activity_main.counterStartValue
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  lateinit var counterRepository: CounterRepository
    @Inject set

  private var counterAdapter: CounterListAdapter = CounterListAdapter(mutableListOf())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    AndroidInjection.inject(this)

    setContentView(layout.activity_main)
    Di.analyticsHelper.sendScreenName(this, "MainActivity")

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
    counterList.adapter = counterAdapter
    counterList.layoutManager = LinearLayoutManager(this)
    counterAdapter.getPositionClicks().subscribe { action ->
      when (action.action) {
        ACTION.DELETE -> onItemDelete(action.item, action.position)
        ACTION.ITEM_CLICKED -> onItemClicked(action.item, action.position)
        ACTION.EDIT -> onItemEdit(action.item, action.position)
        ACTION.DETAILS -> onDetailsClicked(action.item, action.position, action.holder)
        ACTION.INCREMENT -> onItemIncrement(action.item, action.position)
        ACTION.DECREMENT -> onItemDecrement(action.item, action.position)
      }
    }
    counterRepository.fetchAll().subscribe { list ->
      Timber.d("list update: $list")
      if (list.isEmpty()) availableCountersText.visibility = GONE
      counterAdapter.setItems(list.toList())
    }

  }

  private fun addItem(name: String, value: String) {
    val newCounter = Counter(null, name, value.toInt())
    counterRepository.save(newCounter).subscribe { onSuccess ->
      Timber.d("Counter inserted to DB: $onSuccess")
      counterName.text.clear()
      startAlphaAnimation(counterName, this)
      counterList.scrollToPosition(counterAdapter.itemCount - 1)
    }
  }

  //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
  fun onItemDelete(item: Counter, position: Int) {
    DialogManager.showCounterDeleteDialog(this, item, object : DialogCallback {
      override fun onPositiveClicked() {
        counterRepository.delete(item)
      }

      override fun onNegativeClicked() {

      }
    })
  }

  //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
  fun onItemEdit(item: Counter, position: Int) {
    DialogManager.showCounterEditDialog(this, item, object : DialogCallback {
      override fun onPositiveClicked() {
        counterAdapter.notifyItemChanged(position)
      }

      override fun onNegativeClicked() {
      }

    })
  }

  //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
  fun onItemIncrement(item: Counter, position: Int) {
    item.incrementValue()
    counterRepository.save(item)
  }

  //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
  fun onItemDecrement(item: Counter, position: Int) {
    item.decrementValue()
    counterRepository.save(item)
  }

  //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
  fun onItemClicked(item: Counter, position: Int) {
//NOOP
  }

  //  TODO: move to other class, same in CounterWIdgetConfigurationActivity
  fun onDetailsClicked(item: Counter, position: Int,
      holder: CounterItemViewHolder) {
    val intent = Intent(this, CounterDetailsActivity::class.java)
    intent.putExtra(CounterDetailsActivity.EXTRA_COUNTER, item.id)
    val p1: Pair<View, String> = Pair.create(
        holder.name, "counterNameTransition")
    val p2: Pair<View, String> = Pair.create(
        holder.changeDate, "counterChangeDateTransition")
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2)
    startActivity(intent, options.toBundle())
  }

  override fun onResume() {
    super.onResume()
    setList()
    val decorView = window.decorView
// Hide the status bar.
    val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
    decorView.systemUiVisibility = uiOptions
  }
}
