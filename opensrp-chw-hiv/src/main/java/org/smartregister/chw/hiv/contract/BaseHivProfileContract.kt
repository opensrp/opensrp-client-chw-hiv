package org.smartregister.chw.hiv.contract

import android.content.Context
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.domain.AlertStatus
import org.smartregister.view.contract.BaseProfileContract
import java.util.*

interface BaseHivProfileContract {
    interface View : BaseProfileContract.View {
        val context: Context?
        fun openMedicalHistory()
        fun openUpcomingServices()
        fun openFamilyDueServices()
        fun openHivRegistrationForm()
        fun openFollowUpVisitForm(isEdit: Boolean)
        fun openIndexClientsList(hivMemberObject: HivMemberObject?)
        fun setUpComingServicesStatus(
            service: String?,
            status: AlertStatus?,
            date: Date?
        )

        fun setFamilyStatus(status: AlertStatus?)
        fun setProfileViewDetails(hivMemberObject: HivMemberObject?)
        fun setIndexClientsStatus(boolean: Boolean)
        fun setupFollowupVisitEditViews(isWithin24Hours: Boolean)
        fun updateLastVisitRow(lastVisitDate: Date?)
        fun setFollowUpButtonOverdue()
        fun setFollowUpButtonDue()
        fun hideFollowUpVisitButton()
        fun showFollowUpVisitButton(status: Boolean)
        fun showProgressBar(status: Boolean)
        fun onMemberDetailsReloaded(hivMemberObject: HivMemberObject?)
        fun openIndexContactRegistration()
    }

    interface Presenter {
        val view: View?
        fun refreshProfileData()
        fun refreshProfileHivStatusInfo()
    }

    interface Interactor {
        fun refreshProfileView(
            hivMemberObject: HivMemberObject?,
            isForEdit: Boolean,
            callback: InteractorCallback?
        )

        fun updateProfileHivStatusInfo(
            memberObject: HivMemberObject?,
            callback: InteractorCallback?
        )
    }

    interface InteractorCallback {
        fun refreshProfileTopSection(hivMemberObject: HivMemberObject?)
        fun refreshUpComingServicesStatus(
            service: String?,
            status: AlertStatus?,
            date: Date?
        )

        fun refreshFamilyStatus(status: AlertStatus?)
        fun refreshLastVisit(lastVisitDate: Date?)
    }
}