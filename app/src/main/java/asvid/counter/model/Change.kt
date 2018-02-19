package asvid.counter.model

import java.util.Date

data class Change(
    var preValue: Int,
    var postValue: Int,
    var date: Date)