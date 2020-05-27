package org.smartregister.chw.hiv.presenter

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import org.json.JSONException
import org.json.JSONObject
import org.smartregister.chw.hiv.contract.BaseHivFollowupContract
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.model.BaseHivCommunityFollowupModel
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

open class BaseHivCommunityFollowupPresenter(
    view: BaseHivFollowupContract.View,
    private val viewModelClass: Class<out BaseHivCommunityFollowupModel?>,
    protected var interactor: BaseHivFollowupContract.Interactor
) : BaseHivFollowupContract.Presenter, BaseHivFollowupContract.InteractorCallBack {

    private val viewReference = WeakReference(view)
    var hivMemberObject: HivMemberObject? = null

    override fun saveForm(valuesHashMap: HashMap<String, NFormViewData>, jsonObject: JSONObject) =
        try {
            interactor.saveFollowup(hivMemberObject!!.baseEntityId!!, valuesHashMap, jsonObject, this)
        } catch (e: JSONException) {
            Timber.e(Log.getStackTraceString(e))
        } catch (e: SQLiteException) {
            Timber.e(Log.getStackTraceString(e))
        }

    override fun getView(): BaseHivFollowupContract.View? = viewReference.get()

    override fun <T> getViewModel(): Class<T> where T : ViewModel {
        return viewModelClass as Class<T>

    }

    override fun fillProfileData(hivMemberObject: HivMemberObject?) {
        if (hivMemberObject != null && getView() != null) {
            getView()?.setProfileViewWithData()
        }
    }

    override fun onFollowupSaved() = Unit

    override fun initializeMemberObject(hivMemberObject: HivMemberObject) {
        this.hivMemberObject = hivMemberObject
    }
}