package org.smartregister.chw.hiv.presenter

import org.smartregister.chw.hiv.activity.BaseIndexContactsListActivity
import org.smartregister.chw.hiv.contract.BaseIndexClientsContactListContract
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import timber.log.Timber

open class BaseHivIndexcContactsListPresenter(
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


    override fun onDataFetched(hivIndexContactObjectList: List<HivIndexContactObject?>) {
        with(view) {
            displayLoadingState(false)
            refreshIndexList(hivIndexContactObjectList)
        }
    }


}