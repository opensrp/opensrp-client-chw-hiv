package org.smartregister.chw.hiv.contract

import android.content.Context
import org.smartregister.chw.hiv.domain.HivIndexContactObject

/**
 * Contract for BaseIndexContactsListActivity
 */
interface BaseIndexClientsContactListContract {
    /**
     * Interactor callback implementation
     */
    interface InteractorCallBack {
        fun onDataFetched(hivIndexContactObjectList: List<HivIndexContactObject?>)
    }

    /**
     * Implementation for BaseIndexClientsContactListContract Interactor
     */
    interface Interactor {
        /**
         * retrieves the index contacts of [hivClientBaseEntityId]  and returns them to the [callBack]
         */
        fun getClientIndexes(
                hivClientBaseEntityId: String?,
                context: Context?,
                callBack: InteractorCallBack?
        )
    }

    /**
     * Implementation for BaseIndexClientsContactListContract Presenter
     */
    interface Presenter {
        /**
         * initializes the [Presenter]
         */
        fun initialize()

        /**
         * retrieves the [View]
         */
        val view: View?

        /**
         * opens the index contact client [hivIndexContactObject] profile activity
         */
        fun openIndexContactProfile(hivIndexContactObject: HivIndexContactObject?)
    }

    /**
     * defines methods to implement for the view.
     */
    interface View {
        /**
         * initializes the [Presenter]
         */
        fun initializePresenter()

        /**
         * Used to get the instance for [Presenter]
         */
        val presenter: Presenter?

        /**
         * Used to get the instance for [Context]
         */
        val viewContext: Context?

        /**
         * Used to refresh the index contacts list
         */
        fun refreshIndexList(hivIndexContactObjects: List<HivIndexContactObject?>)

        /**
         * Used to display a loading drawable while loading data or refreshing data on the listView
         */
        fun displayLoadingState(var1: Boolean)
    }
}