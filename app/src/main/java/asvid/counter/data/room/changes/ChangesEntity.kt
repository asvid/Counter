package asvid.counter.data.room.changes

import android.arch.persistence.room.Entity
import asvid.counter.data.room.changes.ChangesEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class ChangesEntity {

  companion object {
    const val TABLE_NAME = "changes"
  }
}