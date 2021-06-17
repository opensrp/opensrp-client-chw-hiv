package org.smartregister.chw.hiv.interactor

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import org.smartregister.chw.anc.util.AppExecutors
import org.smartregister.chw.hiv.contract.BaseIndexClientsListContract
import org.smartregister.chw.hiv.dao.HivIndexDao
import org.smartregister.chw.hiv.domain.HivIndexObject
import timber.log.Timber

open class BaseHivClientIndexListInteractor @VisibleForTesting internal constructor(
    var appExecutors: AppExecutors
) : BaseIndexClientsListContract.Interactor {

    constructor() : this(AppExecutors()) {}

    override fun getClientIndexes(
        hivClientBaseEntityId: String?,
        context: Context?,
        callBack: BaseIndexClientsListContract.InteractorCallBack?
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
    ): List<HivIndexObject?> {
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return hivClientBaseEntityId?.let { HivIndexDao.getHivClientIndexes(it) }!!

    }

}