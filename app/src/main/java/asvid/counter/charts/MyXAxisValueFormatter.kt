package asvid.counter.charts

import android.content.Context
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Date

class MyXAxisValueFormatter(val refTime: Long, val context: Context) : IAxisValueFormatter {

  override fun getFormattedValue(v: Float, axisBase: AxisBase): String {
    try {
      val current = context.resources.configuration.locale
      val sdf = SimpleDateFormat("dd/MM/yy", current)
      return sdf.format(Date(v.toLong() + refTime))
    } catch (e: Exception) {
      return v.toString()
    }

  }
}
