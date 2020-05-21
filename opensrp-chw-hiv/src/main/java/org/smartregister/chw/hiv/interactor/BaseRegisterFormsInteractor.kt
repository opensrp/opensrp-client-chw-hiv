package org.smartregister.chw.hiv.interactor

import com.google.gson.Gson
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.json.JSONObject
import org.koin.core.inject
import org.smartregister.chw.hiv.HivLibrary
import org.smartregister.chw.hiv.contract.BaseRegisterFormsContract
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.JsonFormUtils
import org.smartregister.chw.hiv.util.Util.processEvent
import timber.log.Timber
import java.util.*

/**
 * This interactor class provides actual implementations for all the functionality used in the
 * Referral forms, it implements [BaseRegisterFormsContract.Interactor]
 */
class BaseRegisterFormsInteractor : BaseRegisterFormsContract.Interactor {

    val hivLibrary by inject<HivLibrary>()


    @Throws(Exception::class)
    override fun saveRegistration(
        baseEntityId: String, valuesHashMap: HashMap<String, NFormViewData>,
        jsonObject: JSONObject, callBack: BaseRegisterFormsContract.InteractorCallBack
    ) {
        val event =
            JsonFormUtils.processJsonForm(
                hivLibrary, baseEntityId, valuesHashMap,
                jsonObject, Constants.EventType.REGISTRATION
            )
        Timber.i("Event = %s", Gson().toJson(event))
        processEvent(hivLibrary, event)

        callBack.onRegistrationSaved(true)
    }

}