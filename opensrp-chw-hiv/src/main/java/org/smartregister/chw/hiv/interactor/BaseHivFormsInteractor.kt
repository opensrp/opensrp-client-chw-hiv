package org.smartregister.chw.hiv.interactor

import com.google.gson.Gson
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.json.JSONObject
import org.koin.core.inject
import org.smartregister.chw.anc.util.NCUtils
import org.smartregister.chw.hiv.HivLibrary
import org.smartregister.chw.hiv.contract.BaseHivFormsContract
import org.smartregister.chw.hiv.dao.HivDao
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.JsonFormConstants
import org.smartregister.chw.hiv.util.JsonFormUtils
import timber.log.Timber
import java.util.*

/**
 * This interactor class provides actual implementations for all the functionality used in the
 * Referral forms, it implements [BaseHivFormsContract.Interactor]
 */
open class BaseHivFormsInteractor : BaseHivFormsContract.Interactor {

    val hivLibrary by inject<HivLibrary>()


    @Throws(Exception::class)
    override fun saveRegistration(
        baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
        jsonObject: JSONObject, callBack: BaseHivFormsContract.InteractorCallBack
    ) {
        val event =
            JsonFormUtils.processJsonForm(
                hivLibrary, baseEntityId, valuesHashMap,
                jsonObject, jsonObject.getString(JsonFormConstants.ENCOUNTER_TYPE)
            )
        JsonFormUtils.tagEvent(hivLibrary, event)
        when {
            jsonObject.getString(JsonFormConstants.ENCOUNTER_TYPE) == Constants.EventType.HIV_OUTCOME ||
                    jsonObject.getString(JsonFormConstants.ENCOUNTER_TYPE) == Constants.EventType.HIV_COMMUNITY_FOLLOWUP ||
                    jsonObject.getString(JsonFormConstants.ENCOUNTER_TYPE) == Constants.EventType.HIV_INDEX_CONTACT_COMMUNITY_FOLLOWUP ||
                    jsonObject.getString(JsonFormConstants.ENCOUNTER_TYPE) == Constants.EventType.HIV_INDEX_CONTACT_TESTING_FOLLOWUP
            -> event.locationId =
                HivDao.getSyncLocationId(baseEntityId) //Necessary for syncing the event back to the chw
        }
        Timber.i("Event = %s", Gson().toJson(event))
        NCUtils.processEvent(
            event.baseEntityId,
            JSONObject(org.smartregister.chw.anc.util.JsonFormUtils.gson.toJson(event))
        )
        callBack.onRegistrationSaved(true, jsonObject.getString(JsonFormConstants.ENCOUNTER_TYPE))
    }

}