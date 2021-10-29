package org.smartregister.chw.hiv.presenter

import org.smartregister.chw.hiv.activity.BaseHivProfileActivity
import org.smartregister.chw.hiv.contract.BaseHivProfileContract
import org.smartregister.chw.hiv.contract.BaseHivProfileContract.InteractorCallback
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.domain.AlertStatus
import org.smartregister.view.contract.BaseProfileContract
import java.util.*

open class BaseHivProfilePresenter(
    override val view: BaseHivProfileContract.View?,
    val interactor: BaseHivProfileContract.Interactor,
    var hivMemberObject: HivMemberObject
) : BaseProfileContract, BaseHivProfileContract.Presenter,
    InteractorCallback {
    override fun refreshProfileData() {
        view?.showFollowUpVisitButton(true)
        interactor.refreshProfileView(hivMemberObject, false, this)
    }

    override fun refreshProfileHivStatusInfo() {
        interactor.updateProfileHivStatusInfo(hivMemberObject, this)
    }

    override fun refreshLastVisit(lastVisitDate: Date?) {
        view?.updateLastVisitRow(lastVisitDate)
    }

    override fun refreshProfileTopSection(hivMemberObject: HivMemberObject?) {
        view?.setProfileViewDetails(hivMemberObject)
        view?.showProgressBar(false)
    }

    override fun refreshUpComingServicesStatus(
        service: String?,
        status: AlertStatus?,
        date: Date?
    ) {
        view?.setUpComingServicesStatus(service, status, date)
    }

    override fun refreshFamilyStatus(status: AlertStatus?) {
        view?.setFamilyStatus(status)
    }
}