package asvid.counter.charts

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Date

class MyXAxisValueFormatter(val refTime: Long) : IAxisValueFormatter {

    override fun getFormattedValue(v: Float, axisBase: AxisBase): String {
        try {
            val sdf = SimpleDateFormat("dd/MM")
            return sdf.format(Date(v.toLong() + refTime))
        } catch (e: Exception) {
            return v.toString()
        }

    }
}
