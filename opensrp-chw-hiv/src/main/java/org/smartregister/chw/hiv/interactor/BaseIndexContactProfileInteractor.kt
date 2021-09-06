package org.smartregister.chw.hiv.interactor

import androidx.annotation.VisibleForTesting
import org.smartregister.chw.anc.util.AppExecutors
import org.smartregister.chw.hiv.contract.BaseIndexContactProfileContract
import org.smartregister.chw.hiv.contract.BaseIndexContactProfileContract.InteractorCallback
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.domain.AlertStatus
import java.util.*

/**
 * Created by cozej4 on 2021-07-13.
 *
 * @cozej4 https://github.com/cozej4
 *
 * This is the presenter for  [org.smartregister.chw.hiv.activity.BaseIndexContactProfileActivity]
 */
open class BaseIndexContactProfileInteractor @VisibleForTesting internal constructor(
    var appExecutors: AppExecutors
) : BaseIndexContactProfileContract.Interactor {

    constructor() : this(AppExecutors())

    override fun refreshProfileView(
        hivIndexContactObject: HivIndexContactObject?,
        isForEdit: Boolean,
        callback: InteractorCallback?
    ) {
        val runnable = Runnable {
            appExecutors.mainThread()
                .execute { callback!!.refreshProfileTopSection(hivIndexContactObject) }
        }
        appExecutors.diskIO().execute(runnable)
    }

}