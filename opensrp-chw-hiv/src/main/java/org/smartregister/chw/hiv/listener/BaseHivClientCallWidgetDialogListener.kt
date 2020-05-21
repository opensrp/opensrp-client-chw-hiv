package org.smartregister.chw.hiv.listener

import android.view.View
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.fragment.BaseHivClientCallDialogFragment
import org.smartregister.chw.hiv.util.Util
import timber.log.Timber

/**
 * This is the listener implementation for the provided [callDialogFragment]. It handles the click listeners
 */
class BaseHivClientCallWidgetDialogListener(private val callDialogFragment: BaseHivClientCallDialogFragment) :
    View.OnClickListener {
    override fun onClick(view: View) {
        when (view.id) {
            R.id.malaria_call_close -> {
                callDialogFragment.dismiss()
            }
            R.id.hiv_call_head_phone, R.id.call_hiv_client_phone -> {
                try {
                    val phoneNumber = view.tag as String
                    Util.launchDialer(callDialogFragment.activity, callDialogFragment, phoneNumber)
                    callDialogFragment.dismiss()
                } catch (e: IllegalStateException) {
                    Timber.e(e)
                }
            }
        }
    }

}