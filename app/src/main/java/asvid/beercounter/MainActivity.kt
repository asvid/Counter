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
import asvid.beercounter.data.CounterItemManager
import com.thebluealliance.spectrum.SpectrumDialog
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), CounterListListener {

    private var counterAdapter: CounterListAdapter by Delegates.notNull()
    private var counterList: RecyclerView by Delegates.notNull()

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
        val itemList = CounterItemManager.getAllCounterItems()
        counterAdapter = CounterListAdapter(itemList, this)

        counterList = findViewById(R.id.counterList) as RecyclerView
        counterList.adapter = counterAdapter
        counterList.layoutManager = LinearLayoutManager(this)
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
}
