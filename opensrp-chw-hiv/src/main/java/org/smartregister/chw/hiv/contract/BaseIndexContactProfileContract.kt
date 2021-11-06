package org.smartregister.chw.hiv.contract

import android.content.Context
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.view.contract.BaseProfileContract

/**
 * Contract for [org.smartregister.chw.hiv.activity.BaseIndexContactProfileActivity]
 */
interface BaseIndexContactProfileContract {
    /**
     * defines methods to implement for the view.
     */
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

    /**
     * Presenter contract for hiv index contact profile
     */
    interface Presenter {
        /**
         * retrieves the [View]
         */
        val view: View?

        /**
         * resets the client profile info on the activity of the [View]
         */
        fun refreshProfileData()
    }

    /**
     * Interactor implementation
     */
    interface Interactor {
        fun refreshProfileView(
                hivIndexContactObject: HivIndexContactObject?,
                isForEdit: Boolean,
                callback: InteractorCallback?
        )
    }

    /**
     * Interactor callback implementation
     */
    interface InteractorCallback {
        fun refreshProfileTopSection(hivIndexContactObject: HivIndexContactObject?)
    }
}