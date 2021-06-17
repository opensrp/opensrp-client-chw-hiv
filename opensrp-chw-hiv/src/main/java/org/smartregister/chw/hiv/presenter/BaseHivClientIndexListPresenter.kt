package org.smartregister.chw.hiv.presenter

import org.smartregister.chw.hiv.activity.BaseHivClientIndexListActivity
import org.smartregister.chw.hiv.contract.BaseIndexClientsListContract
import org.smartregister.chw.hiv.domain.HivIndexObject
import org.smartregister.chw.hiv.domain.HivMemberObject
import timber.log.Timber

open class BaseHivClientIndexListPresenter(
    val hivClientBaseEntityId: String,
    val interactor: BaseIndexClientsListContract.Interactor,
    override val view: BaseIndexClientsListContract.View
) : BaseIndexClientsListContract.Presenter,
    BaseIndexClientsListContract.InteractorCallBack {

    override fun initialize() {
        Timber.d("initializing presenter")
        view.displayLoadingState(true)


        Timber.d("calling interactor")
        interactor.getClientIndexes(
            hivClientBaseEntityId,
            (view as BaseHivClientIndexListActivity?)!!.viewContext,
            this
        )
    }


    override fun onDataFetched(hivIndexObjectList: List<HivIndexObject?>) {
        with(view) {
            displayLoadingState(false)
            refreshIndexList(hivIndexObjectList)
        }
    }


}