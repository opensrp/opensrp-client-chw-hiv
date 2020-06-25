package org.smartregister.chw.hiv.model

import org.smartregister.chw.hiv.contract.BaseHivRegisterContract

open class BaseHivRegisterModel :
    BaseHivRegisterContract.Model {

    override fun registerViewConfigurations(viewIdentifiers: List<String?>?) = Unit

    override fun unregisterViewConfiguration(viewIdentifiers: List<String?>?) = Unit

    override fun saveLanguage(language: String?) = Unit

    override fun getLocationId(locationName: String?): String? = null

    override val initials: String?
        get() = null
}