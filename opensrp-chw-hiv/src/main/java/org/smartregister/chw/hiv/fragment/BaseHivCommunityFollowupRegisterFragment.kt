package org.smartregister.chw.hiv.fragment

import org.smartregister.chw.hiv.contract.BaseHivRegisterFragmentContract
import org.smartregister.chw.hiv.model.BaseHivCommunityFollowupModel
import org.smartregister.chw.hiv.presenter.BaseHivCommunityFollowupPresenter
import org.smartregister.commonregistry.CommonPersonObjectClient
import org.smartregister.view.fragment.BaseRegisterFragment

/**
 * This register displays list of all the referred clients, it implements [BaseHivRegisterFragmentContract.View] and extends
 * OpenSRP's [BaseRegisterFragment] which provides common functionality and consistency when creating
 * register.
 *
 */
open class BaseHivCommunityFollowupRegisterFragment : BaseHivRegisterFragment() {
    override fun initializePresenter() {
        if (activity == null) {
            return
        }
        presenter =
            BaseHivCommunityFollowupPresenter(this, BaseHivCommunityFollowupModel(), null)
    }

    override fun openProfile(client: CommonPersonObjectClient?) = Unit
}