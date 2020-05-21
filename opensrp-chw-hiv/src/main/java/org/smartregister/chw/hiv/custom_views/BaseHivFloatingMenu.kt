package org.smartregister.chw.hiv.custom_views

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.fragment.BaseHivClientCallDialogFragment.Companion.launchDialog
import org.smartregister.chw.hiv.util.Util.getFullName

class BaseHivFloatingMenu(context: Context?, val hivMemberObject: HivMemberObject) :
    LinearLayout(context), View.OnClickListener {
    fun initUi() {
        View.inflate(context, R.layout.hiv_call_floating_menu, this)
        val fab: FloatingActionButton = findViewById(R.id.hiv_fab)
        fab.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.hiv_fab) {
            val activity = context as Activity
            launchDialog(
                activity,
                getFullName(hivMemberObject),
                hivMemberObject.phoneNumber,
                hivMemberObject.primaryCareGiver,
                hivMemberObject.primaryCareGiverPhoneNumber
            )
        }
    }

    init {
        initUi()
    }
}