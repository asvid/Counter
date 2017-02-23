package asvid.beercounter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import asvid.beercounter.data.CounterItem
import com.thebluealliance.spectrum.SpectrumDialog

class MainActivity : AppCompatActivity(), CounterListListener {

    private var mAdapter: CounterListAdapter? = null
    private var counterList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setList()

        val name = findViewById(R.id.name) as EditText
        val value = findViewById(R.id.value) as EditText
        val addButton = findViewById(R.id.addButton) as Button

        addButton.setOnClickListener { addItem(name.text.toString(), value.text.toString()) }

        //showColors();
    }

    private fun setList() {
        val itemList = (application as App).storage!!.allItems()
        mAdapter = CounterListAdapter(itemList, this)

        counterList = findViewById(R.id.counterList) as RecyclerView
        counterList!!.adapter = mAdapter
        counterList!!.layoutManager = LinearLayoutManager(this)
    }

    private fun showColors() {
        SpectrumDialog.Builder(this).setColors(R.array.demo_colors)
            .setSelectedColorRes(R.color.md_blue_500)
            .setDismissOnColorSelected(true)
            .setOutlineWidth(2)
            .setOnColorSelectedListener { positiveResult, color ->
                if (positiveResult) {
                    Toast.makeText(this@MainActivity,
                        "Color selected: #" + Integer.toHexString(color).toUpperCase(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Dialog cancelled", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .build()
            .show(supportFragmentManager, "dialog_demo_1")
    }

    private fun addItem(name: String, value: String) {
        var value = value
        val counterItem = CounterItem()
        counterItem.name = name
        if (TextUtils.isEmpty(value)) value = "0"
        counterItem.value = Integer.parseInt(value)
        (application as App).storage!!.saveItem(counterItem)
        mAdapter!!.addItem(counterItem)
        counterList!!.adapter = mAdapter
    }

    override fun onItemDelete(item: CounterItem) {

    }

    override fun onItemClicked(item: CounterItem) {

    }

    override fun onItemIncrement(item: CounterItem) {

    }

    override fun onItemDecrement(item: CounterItem) {

    }
}
