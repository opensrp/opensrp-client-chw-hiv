package org.smartregister.chw.hiv.interactor

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.json.JSONObject
import org.koin.core.inject
import org.smartregister.chw.hiv.HivLibrary
import org.smartregister.chw.hiv.contract.BaseHivFollowupContract
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.JsonFormUtils
import timber.log.Timber
import java.util.*


/**
 * This interactor class provides actual implementations for all the functionality used in the
 * follow up referral forms, it implements [BaseHivFollowupContract.Interactor]
 */
class BaseReferralFollowupInteractor : BaseHivFollowupContract.Interactor {

    val hivLibrary by inject<HivLibrary>()

    @Throws(Exception::class)
    override fun saveFollowup(
        baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
        jsonObject: JSONObject, callBack: BaseHivFollowupContract.InteractorCallBack
    ) = saveFollowup(baseEntityId, valuesHashMap, jsonObject)

    @VisibleForTesting
    fun saveFollowup(
        baseEntityId: String?, valuesHashMap: HashMap<String, NFormViewData>?,
        jsonObject: JSONObject?
    ) {
        val baseEvent =
            JsonFormUtils.processJsonForm(
                hivLibrary, baseEntityId, valuesHashMap!!, jsonObject,
                Constants.EventType.REGISTRATION
            )
        baseEvent.eventId = UUID.randomUUID().toString()
        Timber.i("Followup Event = %s", Gson().toJson(baseEvent))
        //        Util.processEvent(allSharedPreferences, baseEvent);
    }
}