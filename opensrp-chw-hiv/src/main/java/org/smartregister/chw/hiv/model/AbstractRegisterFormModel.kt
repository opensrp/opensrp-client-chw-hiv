package org.smartregister.chw.hiv.model

import androidx.lifecycle.ViewModel
import org.smartregister.chw.hiv.contract.BaseRegisterFormsContract
import org.smartregister.chw.hiv.domain.HivMemberObject

abstract class AbstractRegisterFormModel : ViewModel(),
    BaseRegisterFormsContract.Model {

    var hivMemberObject: HivMemberObject? = null
}