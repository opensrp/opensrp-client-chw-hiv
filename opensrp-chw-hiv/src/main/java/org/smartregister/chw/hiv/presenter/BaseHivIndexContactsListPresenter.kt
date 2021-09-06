package org.smartregister.chw.hiv.presenter

import org.smartregister.chw.hiv.activity.BaseIndexContactsListActivity
import org.smartregister.chw.hiv.contract.BaseIndexClientsContactListContract
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import timber.log.Timber

/**
 * Created by cozej4 on 2021-07-13.
 *
 * @cozej4 https://github.com/cozej4
 *
 * This is the presenter for  [org.smartregister.chw.hiv.activity.BaseIndexContactsListActivity]
 */
open class BaseHivIndexContactsListPresenter(
    val hivClientBaseEntityId: String,
    val interactor: BaseIndexClientsContactListContract.Interactor,
    override val view: BaseIndexClientsContactListContract.View
) : BaseIndexClientsContactListContract.Presenter,
    BaseIndexClientsContactListContract.InteractorCallBack {

    override fun initialize() {
        Timber.d("initializing presenter")
        view.displayLoadingState(true)


        Timber.d("calling interactor")
        interactor.getClientIndexes(
            hivClientBaseEntityId,
            (view as BaseIndexContactsListActivity?)!!.viewContext,
            this
        )
    }

    override fun openIndexContactProfile(hivIndexContactObject: HivIndexContactObject?) {
        TODO("Not yet implemented")
    }


    override fun onDataFetched(hivIndexContactObjectList: List<HivIndexContactObject?>) {
        with(view) {
            displayLoadingState(false)
            refreshIndexList(hivIndexContactObjectList)
        }
    }


}