package asvid.counter.data.data_migration

import io.realm.DynamicRealm

interface VersionMigration {
    fun migrate(realm: DynamicRealm, oldVersion: Long)
}