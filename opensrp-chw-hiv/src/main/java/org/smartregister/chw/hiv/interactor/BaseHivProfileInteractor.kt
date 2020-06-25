package org.smartregister.chw.hiv.interactor

import androidx.annotation.VisibleForTesting
import org.smartregister.chw.anc.util.AppExecutors
import org.smartregister.chw.hiv.contract.BaseHivProfileContract
import org.smartregister.chw.hiv.contract.BaseHivProfileContract.InteractorCallback
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.domain.AlertStatus
import java.util.*

open class BaseHivProfileInteractor @VisibleForTesting internal constructor(
    var appExecutors: AppExecutors
) : BaseHivProfileContract.Interactor {

    constructor() : this(AppExecutors()) {}

    override fun refreshProfileView(
        hivMemberObject: HivMemberObject?,
        isForEdit: Boolean,
        callback: InteractorCallback?
    ) {
        val runnable = Runnable {
            appExecutors.mainThread()
                .execute { callback!!.refreshProfileTopSection(hivMemberObject) }
        }
        appExecutors.diskIO().execute(runnable)
    }

    override fun updateProfileHivStatusInfo(
        memberObject: HivMemberObject?,
        callback: InteractorCallback?
    ) {
        val runnable = Runnable {
            appExecutors.mainThread().execute {
                callback!!.refreshFamilyStatus(AlertStatus.normal)
                callback.refreshUpComingServicesStatus(
                    "HIV Followup Visit",
                    AlertStatus.normal,
                    Date()
                )
                callback.refreshLastVisit(Date())
            }
        }
        appExecutors.diskIO().execute(runnable)
    }

}