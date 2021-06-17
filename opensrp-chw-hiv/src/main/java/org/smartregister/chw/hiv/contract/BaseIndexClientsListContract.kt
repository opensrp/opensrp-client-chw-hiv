package org.smartregister.chw.hiv.contract

import android.content.Context
import org.smartregister.chw.hiv.domain.HivIndexObject


interface BaseIndexClientsListContract {
    interface InteractorCallBack {
        fun onDataFetched(hivIndexObjectList: List<HivIndexObject?>)
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
        fun refreshIndexList(hivIndexObjects: List<HivIndexObject?>)
        fun displayLoadingState(var1: Boolean)
    }
}