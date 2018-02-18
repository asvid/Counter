package asvid.counter.modules.widget_list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import asvid.counter.di.Di
import asvid.counter.R
import asvid.counter.R.string
import kotlinx.android.synthetic.main.activity_widget_list.widgetList

class WidgetListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_list)
        setActionBar()
    }

    private fun setList() {
        widgetList.adapter = WidgetListAdapter(Di.storage.getAllWidgets(), this,
            supportFragmentManager)
        widgetList.layoutManager = LinearLayoutManager(this)
        widgetList.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
    }

    private fun setActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = resources.getString(string.widget_list)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        setList()
    }
}
