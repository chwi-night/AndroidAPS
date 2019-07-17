package info.nightscout.androidaps.database.transactions

import info.nightscout.androidaps.database.AppRepository
import info.nightscout.androidaps.database.embedments.InterfaceIDs
import info.nightscout.androidaps.database.entities.Bolus
import info.nightscout.androidaps.database.entities.Carbs
import info.nightscout.androidaps.database.entities.links.MealLink
import java.util.*

class InsightMealBolusTransaction(
        val pumpSerial: String,
        val timestamp: Long,
        val insulin: Double,
        val carbs: Double,
        bolusId: Int,
        val smb: Boolean
) : Transaction<Unit>() {

    val bolusId = bolusId.toLong()

    override fun run() {
        val utcOffset = TimeZone.getDefault().getOffset(timestamp).toLong()
        val bolusDBId = AppRepository.database.bolusDao.insertNewEntry(Bolus(
                timestamp = timestamp,
                utcOffset = utcOffset,
                amount = insulin,
                type = if (smb) Bolus.Type.SMB else Bolus.Type.NORMAL,
                basalInsulin = false
        ).apply {
            interfaceIDs.pumpType = InterfaceIDs.PumpType.ACCU_CHEK_INSIGHT
            interfaceIDs.pumpSerial = pumpSerial
            interfaceIDs.pumpId = bolusId
        })
        if (carbs > 0) {
            val carbsDBId = AppRepository.database.carbsDao.insertNewEntry(Carbs(
                    timestamp = timestamp,
                    utcOffset = utcOffset,
                    amount = carbs,
                    duration = 0
            ))
            AppRepository.database.mealLinkDao.insertNewEntry(MealLink(
                    bolusId = bolusDBId,
                    carbsId = carbsDBId
            ))
        }
    }
}