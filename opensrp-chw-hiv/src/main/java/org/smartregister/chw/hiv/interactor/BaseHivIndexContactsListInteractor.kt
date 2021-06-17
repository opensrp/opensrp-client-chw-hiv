package org.smartregister.chw.hiv.interactor

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import org.smartregister.chw.anc.util.AppExecutors
import org.smartregister.chw.hiv.contract.BaseIndexClientsContactListContract
import org.smartregister.chw.hiv.dao.HivIndexDao
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import timber.log.Timber

open class BaseHivIndexContactsListInteractor @VisibleForTesting internal constructor(
    var appExecutors: AppExecutors
) : BaseIndexClientsContactListContract.Interactor {

    constructor() : this(AppExecutors()) {}

    override fun getClientIndexes(
        hivClientBaseEntityId: String?,
        context: Context?,
        callBack: BaseIndexClientsContactListContract.InteractorCallBack?
    ) {
        val runnable = Runnable {
            val indexClients = getIndexClient(context, hivClientBaseEntityId)

            Timber.d("IndexList = "+ Gson().toJson(indexClients))

            appExecutors.mainThread()
                .execute { callBack!!.onDataFetched(indexClients) }
        }
        appExecutors.diskIO().execute(runnable)
    }

    protected fun getIndexClient(
        context: Context?,
        hivClientBaseEntityId: String?
    ): List<HivIndexContactObject?> {
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return hivClientBaseEntityId?.let { HivIndexDao.getIndexContacts(it) }!!

    }

}