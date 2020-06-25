package org.smartregister.chw.hiv.presenter

import org.apache.commons.lang3.StringUtils
import org.smartregister.chw.hiv.contract.BaseHivRegisterFragmentContract
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.DBConstants

open class BaseHivCommunityFollowupPresenter(
    view: BaseHivRegisterFragmentContract.View,
    model: BaseHivRegisterFragmentContract.Model,
    viewConfigurationIdentifier: String?
) : BaseHivRegisterFragmentPresenter(
    view, model, viewConfigurationIdentifier
) {
    override fun getDefaultSortQuery() =
        Constants.Tables.HIV_COMMUNITY_FOLLOWUP + "." + DBConstants.Key.LAST_INTERACTED_WITH + " DESC "

    override fun initializeQueries(mainCondition: String) {
        val tableName = Constants.Tables.HIV_COMMUNITY_FOLLOWUP
        val condition =
            if (StringUtils.trim(getMainCondition()) == "") mainCondition else getMainCondition()
        val countSelect = model.countSelect(tableName, condition)
        val mainSelect = model.mainSelect(tableName, condition)
        getView()?.also {
            it.initializeQueryParams(tableName, countSelect, mainSelect)
            it.initializeAdapter(visibleColumns)
            it.countExecute()
            it.filterandSortInInitializeQueries()
        }
    }

    override fun getMainTable() = Constants.Tables.HIV_COMMUNITY_FOLLOWUP

    override fun getDueFilterCondition() =
        "${Constants.Tables.HIV_COMMUNITY_FOLLOWUP}.${DBConstants.Key.IS_CLOSED} = 0"

}