package org.smartregister.chw.hiv.presenter

import org.smartregister.chw.hiv.contract.BaseIndexContactProfileContract
import org.smartregister.chw.hiv.contract.BaseIndexContactProfileContract.InteractorCallback
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.domain.AlertStatus
import org.smartregister.view.contract.BaseProfileContract
import timber.log.Timber
import java.util.*

/**
 * Created by cozej4 on 2021-07-13.
 *
 * @cozej4 https://github.com/cozej4
 *
 * This is the presenter for  [org.smartregister.chw.hiv.activity.BaseIndexContactProfileActivity]
 */
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