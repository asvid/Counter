package asvid.counter.data.data_migration

import io.realm.DynamicRealm
import io.realm.RealmObjectSchema
import timber.log.Timber
import java.util.Date

internal class Version1Migration : VersionMigration {

    /************************************************
     * // Version 2
     * class CounterWidget
     * Date createDate //added
     */
    override fun migrate(realm: DynamicRealm, oldVersion: Long) {
        if (oldVersion.equals(1)) {
            val recipeSchema = getObjectSchema(realm)
            recipeSchema.addField(CounterWidgetFields.CREATE_DATE, Date::class.java)
                .transform { obj -> obj.setDate(CounterWidgetFields.CREATE_DATE, Date()) }

            Timber.d("migration complete")
        }
    }

    fun getObjectSchema(realm: DynamicRealm): RealmObjectSchema {
        val schema = realm.schema
        return schema.get(COUNTER_WIDGET)
    }

    object CounterWidgetFields {
        var COLOR = "color"
        var COUNTER_ITEM = "counterItem"
        var CREATE_DATE = "createDate"
    }

    companion object {
        private val COUNTER_WIDGET = "CounterWidget"
    }
}
