package org.smartregister.chw.hiv.model

import androidx.lifecycle.ViewModel
import org.smartregister.chw.hiv.domain.HivMemberObject


open class BaseReferralFollowupModel : ViewModel() {
    var hivMemberObject: HivMemberObject? = null
}