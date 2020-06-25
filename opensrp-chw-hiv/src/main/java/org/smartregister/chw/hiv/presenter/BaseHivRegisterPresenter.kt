package org.smartregister.chw.hiv.presenter

import org.smartregister.chw.hiv.contract.BaseHivRegisterContract
import java.lang.ref.WeakReference

open class BaseHivRegisterPresenter(
    view: BaseHivRegisterContract.View,
    protected var model: BaseHivRegisterContract.Model
) : BaseHivRegisterContract.Presenter {

    private var viewReference = WeakReference(view)

    override fun getView() = viewReference.get()

    override fun registerViewConfigurations(list: List<String>?) = Unit

    override fun unregisterViewConfiguration(list: List<String>?) = Unit

    override fun onDestroy(b: Boolean) = Unit

    override fun updateInitials() = Unit

}