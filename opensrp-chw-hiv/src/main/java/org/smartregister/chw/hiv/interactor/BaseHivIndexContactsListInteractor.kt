package org.smartregister.chw.hiv.interactor

import android.content.Context
import androidx.annotation.VisibleForTesting
import org.smartregister.chw.anc.util.AppExecutors
import org.smartregister.chw.hiv.contract.BaseIndexClientsContactListContract
import org.smartregister.chw.hiv.dao.HivIndexDao
import org.smartregister.chw.hiv.domain.HivIndexContactObject

/**
 * Created by cozej4 on 2021-07-13.
 *
 * @cozej4 https://github.com/cozej4
 *
 * This is the interactor for  [org.smartregister.chw.hiv.activity.BaseIndexContactsListActivity]
 * implements [BaseIndexClientsContactListContract.Interactor]
 */
open class BaseHivIndexContactsListInteractor @VisibleForTesting internal constructor(
        var appExecutors: AppExecutors
) : BaseIndexClientsContactListContract.Interactor {

    constructor() : this(AppExecutors())

    override fun getClientIndexes(
            hivClientBaseEntityId: String?,
            context: Context?,
            callBack: BaseIndexClientsContactListContract.InteractorCallBack?
    ) {
        val runnable = Runnable {
            val indexClients = getIndexClient(hivClientBaseEntityId)

            appExecutors.mainThread()
                    .execute { callBack!!.onDataFetched(indexClients) }
        }
        appExecutors.diskIO().execute(runnable)
    }

    protected fun getIndexClient(
            hivClientBaseEntityId: String?
    ): List<HivIndexContactObject?> {
        return hivClientBaseEntityId?.let { HivIndexDao.getIndexContacts(it) }!!

    }

}