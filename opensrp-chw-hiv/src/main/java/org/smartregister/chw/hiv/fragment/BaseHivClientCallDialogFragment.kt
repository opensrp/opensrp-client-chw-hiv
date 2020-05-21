package org.smartregister.chw.hiv.fragment

import android.app.Activity
import android.app.DialogFragment
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import org.apache.commons.lang3.StringUtils
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseHivClientCallDialogContract
import org.smartregister.chw.hiv.listener.BaseHivClientCallWidgetDialogListener
import org.smartregister.util.Utils


/**
 * Fragment used for launching a call dialog; implements [BaseHivClientCallDialogContract.View]
 */
open class BaseHivClientCallDialogFragment : DialogFragment(),
    BaseHivClientCallDialogContract.View {

    private var listener: View.OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle
    ): View {
        val dialogView = inflater.inflate(
            R.layout.referral_member_call_widget_dialog_fragment, container, false
        ) as ViewGroup
        setUpPosition()
        if (listener == null) {
            listener = BaseHivClientCallWidgetDialogListener(this)
        }
        initUI(dialogView)
        return dialogView
    }

    private fun initUI(rootView: ViewGroup) {
        if (StringUtils.isNotBlank(referralClientPhoneNumber)) {
            rootView.findViewById<TextView>(R.id.call_referral_client_name)
                ?.apply { text = referralClientName }

            rootView.findViewById<TextView>(R.id.call_hiv_client_phone)?.apply {
                tag = referralClientPhoneNumber
                text = Utils.getName(getString(R.string.call), referralClientPhoneNumber)
                setOnClickListener(listener)
            }

        } else {
            rootView.findViewById<View>(R.id.layout_malaria_client).visibility = View.GONE
        }
        if (StringUtils.isNotBlank(referralFamilyHeadPhone)) {

            rootView.findViewById<TextView>(R.id.malaria_call_head_name)
                ?.apply { text = referralFamilyHeadName }

            rootView.findViewById<TextView>(R.id.hiv_call_head_phone)
                ?.apply {
                    tag = referralFamilyHeadPhone
                    text = Utils.getName(getString(R.string.call), referralFamilyHeadPhone)
                    setOnClickListener(listener)
                }
        } else {
            rootView.findViewById<View>(R.id.malaria_layout_family_head).visibility = View.GONE
        }
        rootView.findViewById<View>(R.id.malaria_call_close).setOnClickListener(listener)
    }

    private fun setUpPosition() {
        dialog.window?.also {
            it.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
            val p = it.attributes
            with(p) {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                y = 20
            }
            it.attributes = p
        }
    }

    override var pendingCallRequest: BaseHivClientCallDialogContract.Dialer? = null

    companion object {

        const val DIALOG_TAG = "BaseReferralCallDialogFragment_DIALOG_TAG"
        private var referralClientName: String? = null
        private var referralClientPhoneNumber: String? = null
        private var referralFamilyHeadName: String? = null
        private var referralFamilyHeadPhone: String? = null

        fun launchDialog(
            activity: Activity, clientName: String?, referralClientPhone: String?,
            familyHeadName: String?, familyHeadPhone: String?
        ): BaseHivClientCallDialogFragment {
            val dialogFragment = newInstance()
            val ft = activity.fragmentManager.beginTransaction()
            val prev = activity.fragmentManager.findFragmentByTag(DIALOG_TAG)
            referralClientPhoneNumber = referralClientPhone
            referralClientName = clientName
            referralFamilyHeadName = familyHeadName
            referralFamilyHeadPhone = familyHeadPhone
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            dialogFragment.show(ft, DIALOG_TAG)
            return dialogFragment
        }

        fun newInstance(): BaseHivClientCallDialogFragment = BaseHivClientCallDialogFragment()
    }
}