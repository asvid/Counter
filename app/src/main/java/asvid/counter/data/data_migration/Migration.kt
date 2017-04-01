package asvid.counter.data.data_migration

import asvid.counter.data.widget.WidgetSize
import io.realm.DynamicRealm
import io.realm.RealmMigration
import io.realm.RealmObjectSchema
import timber.log.Timber
import java.util.Date

class Migration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var version = oldVersion
        Timber.d("migrate: $oldVersion")
        if (version == 0L) {
            val recipeSchema = getObjectSchema(realm)
            recipeSchema.addField(CounterWidgetFields.CREATE_DATE, Date::class.java)
                .transform { obj -> obj.setDate(CounterWidgetFields.CREATE_DATE, Date()) }
            Timber.d("migration 0 complete")
            version++
        }
        if (version == 1L) {
            val recipeSchema = getObjectSchema(realm)
            val size = WidgetSize()
            size.widthFactor = 1
            size.heightFactor = 1
            recipeSchema.addField(CounterWidgetFields.SIZE, WidgetSize::class.java)
                .transform { obj -> obj.set(CounterWidgetFields.SIZE, size) }
            Timber.d("migration 1 complete")
            version++
        }
    }

    fun getObjectSchema(realm: DynamicRealm): RealmObjectSchema {
        val schema = realm.schema
        return schema.get(COUNTER_WIDGET)
    }

    override fun equals(other: Any?): Boolean {
        return (other is Migration)
    }

    override fun hashCode(): Int {
        return asvid.counter.BuildConfig.REALM_SCHEMA_VERSION.toInt()
    }

    object CounterWidgetFields {
        var COLOR = "color"
        var COUNTER_ITEM = "counterItem"
        var CREATE_DATE = "createDate"
        var SIZE = "size"
    }

    companion object {
        private val COUNTER_WIDGET = "CounterWidget"
    }
}