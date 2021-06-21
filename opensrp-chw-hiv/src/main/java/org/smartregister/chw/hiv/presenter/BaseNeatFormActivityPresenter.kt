package org.smartregister.chw.hiv.presenter

import android.app.Activity
import android.database.sqlite.SQLiteException
import android.util.Log
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.apache.commons.lang3.tuple.Triple
import org.json.JSONException
import org.json.JSONObject
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseNeatFormsContract
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.DBConstants
import org.smartregister.util.Utils
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

open class BaseNeatFormActivityPresenter(
    val baseEntityID: String,
    view: BaseNeatFormsContract.View,
    protected var interactor: BaseNeatFormsContract.Interactor
) : BaseNeatFormsContract.Presenter, BaseNeatFormsContract.InteractorCallBack {

    var hivMemberObject: HivMemberObject? = null
    private var viewReference = WeakReference(view)

    override fun getView(): BaseNeatFormsContract.View? {
        return viewReference.get()
    }

    override fun getMainCondition() =
        "${Constants.Tables.FAMILY_MEMBER}.${DBConstants.Key.BASE_ENTITY_ID}  = '$baseEntityID'"

    override fun getMainTable() = Constants.Tables.FAMILY_MEMBER

    override fun fillClientData(hivMemberObject: HivMemberObject?) {
        if (getView() != null) {
            getView()?.setProfileViewWithData()
        }
    }

    override fun initializeMemberObject(hivMemberObject: HivMemberObject?) {
        this.hivMemberObject = hivMemberObject
    }

    override fun saveForm(valuesHashMap: HashMap<String, NFormViewData>, jsonObject: JSONObject) {
        try {
            interactor.saveRegistration(baseEntityID, valuesHashMap, jsonObject, this)
        } catch (e: JSONException) {
            Timber.e(Log.getStackTraceString(e))
        } catch (e: SQLiteException) {
            Timber.e(Log.getStackTraceString(e))
        }
    }

    override fun onUniqueIdFetched(triple: Triple<String, String, String>, entityId: String) = Unit

    override fun onNoUniqueId() = Unit

    override fun onRegistrationSaved(saveSuccessful: Boolean, encounterType: String) {
        val context = getView() as Activity
        val toastMessage = when {
            saveSuccessful && encounterType == Constants.EventType.REGISTRATION -> context.getString(
                R.string.successful_hiv_registration
            )
            saveSuccessful && encounterType == Constants.EventType.FOLLOW_UP_VISIT -> context.getString(
                R.string.successful_visit
            )
            saveSuccessful && encounterType == Constants.EventType.HIV_OUTCOME -> context.getString(
                R.string.hiv_outcome_saved
            )
            saveSuccessful && encounterType == Constants.EventType.HIV_COMMUNITY_FOLLOWUP -> context.getString(
                R.string.hiv_community_followup_referral_issued
            )
            saveSuccessful && encounterType == Constants.EventType.HIV_COMMUNITY_FOLLOWUP_FEEDBACK -> context.getString(
                R.string.hiv_community_followup_feedback_saved
            )
            saveSuccessful -> context.getString(
                R.string.form_saved
            )
            else -> context.getString(R.string.form_not_saved)
        }
        Utils.showToast(context, toastMessage)
    }
}