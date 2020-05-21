package org.smartregister.chw.hiv.presenter

import android.app.Activity
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.apache.commons.lang3.tuple.Triple
import org.json.JSONException
import org.json.JSONObject
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseRegisterFormsContract
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.model.AbstractRegisterFormModel
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.DBConstants
import org.smartregister.util.Utils
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

open class BaseRegisterFormsPresenter(
    val baseEntityID: String,
    view: BaseRegisterFormsContract.View,
    private val viewModelClass: Class<out AbstractRegisterFormModel>,
    protected var interactor: BaseRegisterFormsContract.Interactor
) : BaseRegisterFormsContract.Presenter, BaseRegisterFormsContract.InteractorCallBack {

    var hivMemberObject: HivMemberObject? = null
    private var viewReference = WeakReference(view)

    override fun getView(): BaseRegisterFormsContract.View? {
        return viewReference.get()
    }

    override fun <T> getViewModel(): Class<T> where T : ViewModel, T : BaseRegisterFormsContract.Model {
        return viewModelClass as Class<T>
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

    override fun onRegistrationSaved(saveSuccessful: Boolean) {
        val context = getView() as Activity
        val toastMessage = if (saveSuccessful) context.getString(R.string.referral_submitted)
        else context.getString(R.string.referral_not_submitted)
        Utils.showToast(context, toastMessage)
    }
}