package asvid.counter.model

import java.util.Date

data class DownCounterWidget(
    val id: Long,
    var name: String,
    var selectedDate: Date,
    var color: Int,
    val size: WidgetSize)