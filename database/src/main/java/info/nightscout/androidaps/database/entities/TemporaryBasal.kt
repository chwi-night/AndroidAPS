package info.nightscout.androidaps.database.entities

import androidx.room.*
import info.nightscout.androidaps.database.TABLE_TEMPORARY_BASALS
import info.nightscout.androidaps.database.embedments.InterfaceIDs
import info.nightscout.androidaps.database.interfaces.DBEntry
import info.nightscout.androidaps.database.interfaces.DBEntryWithTimeAndDuration

@Entity(tableName = TABLE_TEMPORARY_BASALS,
        foreignKeys = [ForeignKey(
                entity = TemporaryBasal::class,
                parentColumns = ["id"],
                childColumns = ["referenceId"])],
        indices = [Index("referenceId"), Index("timestamp")])
data class TemporaryBasal(
        @PrimaryKey(autoGenerate = true)
        override var id: Long = 0,
        override var version: Int = 0,
        override var lastModified: Long = -1,
        override var valid: Boolean = true,
        override var referenceId: Long? = null,
        @Embedded
        override var interfaceIDs_backing: InterfaceIDs? = InterfaceIDs(),
        override var timestamp: Long,
        override var utcOffset: Long,
        var type: Type,
        var absolute: Boolean,
        var rate: Double,
        override var duration: Long
) : DBEntry<TemporaryBasal>, DBEntryWithTimeAndDuration {
    enum class Type {
        NORMAL,
        EMULATED_PUMP_SUSPEND,
        PUMP_SUSPEND,
        SUPERBOLUS
    }
}