package asvid.counter.model

import java.util.Date

data class CounterWidget(
    val id: Long,
    var color: Int,
    var counter: Counter,
    val createDate: Date,
    val size: WidgetSize)