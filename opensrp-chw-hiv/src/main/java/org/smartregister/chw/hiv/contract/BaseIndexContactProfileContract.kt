package org.smartregister.chw.hiv.contract

import android.content.Context
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.domain.AlertStatus
import org.smartregister.view.contract.BaseProfileContract
import java.util.*

interface BaseIndexContactProfileContract {
    interface View : BaseProfileContract.View {
        val context: Context?
        fun openFamilyDueServices()
        fun openHivRegistrationForm()
        fun openFollowUpVisitForm(isEdit: Boolean)
        fun setUpComingServicesStatus(
            service: String?,
            status: AlertStatus?,
            date: Date?
        )

        fun setFamilyStatus(status: AlertStatus?)
        fun setProfileViewDetails(hivIndexContactObject: HivIndexContactObject?)
        fun setupFollowupVisitEditViews(isWithin24Hours: Boolean)
        fun updateLastVisitRow(lastVisitDate: Date?)
        fun setFollowUpButtonOverdue()
        fun setFollowUpButtonDue()
        fun checkFollowupStatus()
        fun hideFollowUpVisitButton()
        fun showFollowUpVisitButton(status: Boolean)
        fun showProgressBar(status: Boolean)
        fun onMemberDetailsReloaded(hivIndexContactObject: HivIndexContactObject?)
    }

    interface Presenter {
        val view: View?
        fun refreshProfileData()
        fun refreshProfileHivStatusInfo()
    }

    interface Interactor {
        fun refreshProfileView(
            hivIndexContactObject: HivIndexContactObject?,
            isForEdit: Boolean,
            callback: InteractorCallback?
        )

        fun updateProfileHivStatusInfo(
            hivIndexContactObject: HivIndexContactObject?,
            callback: InteractorCallback?
        )
    }

    interface InteractorCallback {
        fun refreshProfileTopSection(hivIndexContactObject: HivIndexContactObject?)
        fun refreshUpComingServicesStatus(
            service: String?,
            status: AlertStatus?,
            date: Date?
        )

        fun refreshFamilyStatus(status: AlertStatus?)
        fun refreshLastVisit(lastVisitDate: Date?)
    }
}