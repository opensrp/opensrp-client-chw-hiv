package org.smartregister.chw.hiv.custom_views

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.chw.hiv.fragment.BaseHivClientCallDialogFragment.Companion.launchDialog
import org.smartregister.chw.hiv.util.HivUtil.getFullName

open class BaseIndexFloatingMenu(context: Context?, val hivIndexContactObject: HivIndexContactObject) :
    LinearLayout(context), View.OnClickListener {

    open fun initUi() {
        View.inflate(context, R.layout.hiv_call_floating_menu, this)
        val fab: FloatingActionButton = findViewById(R.id.hiv_fab)
        fab.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.hiv_fab) {
            val activity = context as Activity
            launchDialog(
                activity,
                getFullName(hivIndexContactObject),
                hivIndexContactObject.phoneNumber,
                null,
                null
            )
        }
    }

    init {
        initUi()
    }
}