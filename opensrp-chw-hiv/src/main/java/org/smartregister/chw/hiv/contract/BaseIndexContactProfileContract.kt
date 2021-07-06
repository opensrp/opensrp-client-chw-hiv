package org.smartregister.chw.hiv.contract

import android.content.Context
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.domain.AlertStatus
import org.smartregister.view.contract.BaseProfileContract
import java.util.*

interface BaseIndexContactProfileContract {
    interface View : BaseProfileContract.View {
        val context: Context?
        fun openFollowUpVisitForm(isEdit: Boolean)
        fun setProfileViewDetails(hivIndexContactObject: HivIndexContactObject?)
        fun setupFollowupVisitEditViews(isWithin24Hours: Boolean)
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
    }

    interface Interactor {
        fun refreshProfileView(
            hivIndexContactObject: HivIndexContactObject?,
            isForEdit: Boolean,
            callback: InteractorCallback?
        )
    }

    interface InteractorCallback {
        fun refreshProfileTopSection(hivIndexContactObject: HivIndexContactObject?)
    }
}