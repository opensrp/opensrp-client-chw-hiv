package org.smartregister.chw.hiv.contract

import android.content.Context
import org.smartregister.chw.hiv.domain.HivIndexContactObject


interface BaseIndexClientsContactListContract {
    interface InteractorCallBack {
        fun onDataFetched(hivIndexContactObjectList: List<HivIndexContactObject?>)
    }

    interface Interactor {
        fun getClientIndexes(
            hivClientBaseEntityId: String?,
            context: Context?,
            callBack: InteractorCallBack?
        )
    }

    interface Presenter {
        fun initialize()
        val view: View?
    }

    interface View {
        fun initializePresenter()
        val presenter: Presenter?
        val viewContext: Context?
        fun refreshIndexList(hivIndexContactObjects: List<HivIndexContactObject?>)
        fun displayLoadingState(var1: Boolean)
    }
}