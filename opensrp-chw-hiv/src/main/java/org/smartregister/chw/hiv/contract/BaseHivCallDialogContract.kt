package org.smartregister.chw.hiv.contract

import android.content.Context

interface BaseHivCallDialogContract {
    interface View {
        var pendingCallRequest: Dialer?
        val currentContext: Context?
    }

    interface Dialer {
        fun callMe()
    }
}