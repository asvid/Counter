package asvid.beercounter.data

import android.appwidget.AppWidgetManager
import android.content.Intent
import asvid.beercounter.Di
import asvid.beercounter.Di.context
import widget.BeerCounterWidgetProvider

object CounterItemManager {

    fun incrementAndSave(item: CounterItem) {
        item.incrementValue()
        Di.storage.saveItem(item)
        Di.storage.getWidgetsOfCounter(item).map { updateWidget(it.id) }
    }

    private fun updateWidget(id: Long?) {
        val intent = Intent(context, BeerCounterWidgetProvider::class.java)
        intent.action = BeerCounterWidgetProvider.UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id?.toInt())

        context.sendBroadcast(intent)
    }

    fun decrementAndSave(item: CounterItem) {
        item.decrementValue()
        Di.storage.saveItem(item)
        Di.storage.getWidgetsOfCounter(item).map { updateWidget(it.id) }
    }

    fun getAllCounterItems(): MutableList<CounterItem> {
        return Di.storage.allItems()
    }

    fun getCounterItem(id: Long): CounterItem {
        return Di.storage.getCounterItem(id)
    }

    fun saveCounterItem(counterItem: CounterItem) {
        Di.storage.saveItem(counterItem)
    }

    fun deleteCounterItem(item: CounterItem) {
        Di.storage.deleteCounter(item)
    }

}