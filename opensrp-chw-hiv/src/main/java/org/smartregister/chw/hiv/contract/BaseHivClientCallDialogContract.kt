package org.smartregister.chw.hiv.contract

interface BaseHivClientCallDialogContract {

    interface View {

        var pendingCallRequest: Dialer?

    }

    interface Model {
        var name: String?
    }

    interface Dialer {
        fun callMe()
    }
}