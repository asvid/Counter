package asvid.counter.data

import android.appwidget.AppWidgetManager
import android.content.Intent
import asvid.counter.Di
import asvid.counter.Di.context
import asvid.counter.analytics.enums.Action
import asvid.counter.analytics.enums.Category
import asvid.counter.widget.CounterWidgetProvider

object CounterItemManager {

    fun incrementAndSave(item: CounterItem) {
        item.incrementValue()
        saveAndUpdateWidget(item)
    }

    private fun updateWidget(id: Long?) {
        val intent = Intent(context, CounterWidgetProvider::class.java)
        intent.action = CounterWidgetProvider.UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id?.toInt())

        context.sendBroadcast(intent)
    }

    fun decrementAndSave(item: CounterItem) {
        item.decrementValue()
        saveAndUpdateWidget(item)
    }

    fun saveAndUpdateWidget(item: CounterItem) {
        Di.storage.saveItem(item)
        Di.storage.getWidgetsOfCounter(item).map { updateWidget(it.id) }
    }

    fun getAllCounterItems(): MutableList<CounterItem> {
        val allItems = Di.storage.allItems()
        Di.analyticsHelper.sendEvent(Category.COUNTER, Action.ALL_ITEMS, "${allItems.size}")
        return allItems
    }

    fun getCounterItem(id: Long): CounterItem {
        return Di.storage.getCounterItem(id)
    }

    fun saveCounterItem(counterItem: CounterItem) {
        Di.storage.saveItem(counterItem)
        Di.analyticsHelper.sendEvent(Category.COUNTER, Action.ADD, "")
    }

    fun deleteCounterItem(item: CounterItem) {
        Di.analyticsHelper.sendEvent(Category.COUNTER, Action.DELETE, "")
        Di.storage.getWidgetsOfCounter(item).map { updateWidget(it.id) }
        Di.storage.deleteCounter(item)
    }

}