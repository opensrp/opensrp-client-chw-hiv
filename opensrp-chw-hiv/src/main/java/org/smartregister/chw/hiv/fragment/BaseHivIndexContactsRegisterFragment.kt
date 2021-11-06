package org.smartregister.chw.hiv.fragment

import android.widget.ImageView
import android.widget.TextView
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.model.BaseHivRegisterFragmentModel
import org.smartregister.chw.hiv.presenter.BaseHivIndexContactsRegisterFragmentPresenter
import org.smartregister.view.customcontrols.CustomFontTextView
import org.smartregister.view.customcontrols.FontVariant

/**
 * This register displays list of all the registered index clients, it extends
 *  [BaseHivRegisterFragment] which provides common functionality and consistency when creating hiv
 * registers.
 *
 */
open class BaseHivIndexContactsRegisterFragment : BaseHivRegisterFragment() {


    override fun setupViews(view: android.view.View) {
        super.setupViews(view)
        // Update top left icon
        view.findViewById<ImageView>(org.smartregister.R.id.scanQrCode)?.apply {
            visibility = android.view.View.GONE
        }
        // Update Search bar
        view.findViewById<android.view.View>(org.smartregister.R.id.search_bar_layout)
            ?.apply { setBackgroundResource(R.color.customAppThemeBlue) }

        getSearchView()?.apply {
            getSearchView().setBackgroundResource(R.color.white)
            getSearchView().setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_action_search, 0, 0, 0
            )
        }
        // Update sort filter
        view.findViewById<TextView>(org.smartregister.R.id.filter_text_view)
            ?.apply {
                text = getString(R.string.sort)
            }
        // Update title name
        view.findViewById<ImageView>(org.smartregister.R.id.opensrp_logo_image_view)
            ?.apply {
                visibility = android.view.View.GONE
            }
        view.findViewById<CustomFontTextView>(R.id.txt_title_label)?.apply {
            visibility = android.view.View.VISIBLE
            text = getString(R.string.hiv_index_clients_contacts)
            setFontVariant(FontVariant.REGULAR)
        }
    }

    override fun initializePresenter() {
        if (activity == null) {
            return
        }
        presenter =
            BaseHivIndexContactsRegisterFragmentPresenter(this, BaseHivRegisterFragmentModel(), null)
    }
}