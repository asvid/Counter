package asvid.counter.repository.mappers

import asvid.counter.data.room.counter.CounterEntity
import asvid.counter.data.room.counter.changes.ChangesEntity
import asvid.counter.data.room.widget.WidgetSize
import asvid.counter.data.room.widget.counter.CounterWidgetEntity
import asvid.counter.data.room.widget.downcounter.DownCounterWidgetEntity
import asvid.counter.model.Change
import asvid.counter.model.Counter
import asvid.counter.model.CounterWidget
import asvid.counter.model.DownCounterWidget

fun ChangesEntity.toModel(): Change {
  return Change(this.preValue!!, this.postValue!!, this.date!!)
}

fun Change.toEntity(chounterId: Long): ChangesEntity {
  return ChangesEntity(this.preValue, this.postValue, this.date, chounterId)
}

fun CounterEntity.toModel(): Counter {
  return Counter(this.id!!, this.name, this.value)
}

fun List<CounterEntity>.toModel(): List<Counter> {
  return this.map { it.toModel() }
}

fun Counter.toEntity(): CounterEntity {
  return CounterEntity(this.name, this.value).also { it.id = this.id }
}

fun CounterWidgetEntity.toModel(counterEntity: CounterEntity): CounterWidget {
  return CounterWidget(this.id!!, this.color!!, counterEntity.toModel(), this.createDate!!,
      this.size!!.toModel())
}

fun CounterWidget.toEntity(): CounterWidgetEntity {
  return CounterWidgetEntity().also {
    it.id = this.id
    it.color = this.color
    it.createDate = this.createDate
    it.size = this.size.toEntity()
    it.counterId = this.counter.id
  }
}

fun DownCounterWidgetEntity.toModel(): DownCounterWidget {
  return DownCounterWidget(this.id!!, this.name!!, this.selectedDate!!, this.color!!,
      this.size!!.toModel())
}

fun DownCounterWidget.toEntity(): DownCounterWidgetEntity {
  return DownCounterWidgetEntity().also {
    it.id = this.id
    it.color = this.color
    it.selectedDate = this.selectedDate
    it.size = this.size.toEntity()
  }
}

private fun WidgetSize.toModel(): asvid.counter.model.WidgetSize {
  return asvid.counter.model.WidgetSize(this.widthFactor, this.heightFactor)
}

private fun asvid.counter.model.WidgetSize.toEntity(): WidgetSize {
  return WidgetSize(this.widthFactor, this.heightFactor)
}
