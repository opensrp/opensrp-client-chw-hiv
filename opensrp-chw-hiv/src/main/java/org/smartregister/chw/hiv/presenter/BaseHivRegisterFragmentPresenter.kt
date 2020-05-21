package org.smartregister.chw.hiv.presenter

import org.apache.commons.lang3.StringUtils
import org.smartregister.chw.hiv.contract.BaseHivRegisterFragmentContract
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.DBConstants
import org.smartregister.configurableviews.model.Field
import org.smartregister.configurableviews.model.RegisterConfiguration
import org.smartregister.configurableviews.model.View
import java.lang.ref.WeakReference
import java.util.*

open class BaseHivRegisterFragmentPresenter(
    view: BaseHivRegisterFragmentContract.View,
    protected var model: BaseHivRegisterFragmentContract.Model,
    protected var viewConfigurationIdentifier: String?
) : BaseHivRegisterFragmentContract.Presenter {

    protected var viewReference = WeakReference(view)
    protected var config: RegisterConfiguration
    var visibleColumns: Set<View> = TreeSet()

    override fun updateSortAndFilter(filterList: List<Field>, sortField: Field) = Unit

    override fun getMainCondition() = ""

    override fun getDefaultSortQuery() =
        Constants.Tables.HIV + "." + DBConstants.Key.HIV_REGISTRATION_DATE + " DESC "

    override fun processViewConfigurations() {
        if (StringUtils.isBlank(viewConfigurationIdentifier)) {
            return
        }
        val viewConfiguration =
            model.getViewConfiguration(viewConfigurationIdentifier)
        if (viewConfiguration != null) {
            config = viewConfiguration.metadata as RegisterConfiguration
            visibleColumns = model.getRegisterActiveColumns(viewConfigurationIdentifier)!!
        }
        if (config.searchBarText != null && getView() != null) {
            getView()?.updateSearchBarHint(config.searchBarText)
        }
    }

    override fun initializeQueries(mainCondition: String) {
        val tableName = Constants.Tables.HIV
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

    override fun getView(): BaseHivRegisterFragmentContract.View? {
        return viewReference.get()
    }

    override fun startSync() = Unit

    override fun searchGlobally(s: String) = Unit

    override fun getMainTable() = Constants.Tables.HIV

    override fun getDueFilterCondition() =
        "ec_hiv.hiv_status = '${Constants.HivStatus.UNKNOWN}'"

    init {
        config = model.defaultRegisterConfiguration()!!
    }
}