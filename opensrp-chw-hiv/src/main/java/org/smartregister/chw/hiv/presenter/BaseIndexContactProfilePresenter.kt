package org.smartregister.chw.hiv.presenter

import org.smartregister.chw.hiv.contract.BaseIndexContactProfileContract
import org.smartregister.chw.hiv.contract.BaseIndexContactProfileContract.InteractorCallback
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.domain.AlertStatus
import org.smartregister.view.contract.BaseProfileContract
import timber.log.Timber
import java.util.*

open class BaseIndexContactProfilePresenter(
    override val view: BaseIndexContactProfileContract.View?,
    val interactor: BaseIndexContactProfileContract.Interactor,
    var hivIndexContactObject: HivIndexContactObject
) : BaseProfileContract, BaseIndexContactProfileContract.Presenter,
    InteractorCallback {
    override fun refreshProfileData() {
        Timber.d("RefreshProfileData")
        view?.setFollowUpButtonDue()
        interactor.refreshProfileView(hivIndexContactObject, false, this)
    }

    override fun refreshProfileTopSection(hivIndexContactObject: HivIndexContactObject?) {
        view?.setProfileViewDetails(hivIndexContactObject)
        view?.checkFollowupStatus()
        view?.showProgressBar(false)
    }
}