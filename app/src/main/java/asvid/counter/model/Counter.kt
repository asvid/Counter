package asvid.counter.model

import java.util.Date

data class Counter(var id: Long, var name: String, var value: Int) {

  var changes: MutableList<Change> = mutableListOf()

  fun incrementValue() {
    processChange(value + 1)
  }

  fun decrementValue() {
    processChange(value - 1)
  }

  private fun processChange(postValue: Int) {
    val change = Change(value, postValue, Date())
    changes.add(change)
    this.value = postValue
  }
}