package org.smartregister.chw.hiv.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.smartregister.chw.anc.domain.MemberObject
import org.smartregister.chw.anc.util.Constants.ANC_MEMBER_OBJECTS
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseIndexClientsListContract
import org.smartregister.chw.hiv.domain.HivIndexObject
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.interactor.BaseHivClientIndexListInteractor
import org.smartregister.chw.hiv.presenter.BaseHivClientIndexListPresenter
import org.smartregister.chw.hiv.provider.IndexClientsAdapter
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.view.activity.SecuredActivity
import timber.log.Timber


open class BaseHivClientIndexListActivity : SecuredActivity(),
    BaseIndexClientsListContract.View {
    var memberObject: HivMemberObject? = null
    override fun initializePresenter() {
        TODO("Not yet implemented")
    }

    override var presenter: BaseIndexClientsListContract.Presenter? = null
    var linearLayout: LinearLayout? = null
    var tvTitle: TextView? = null
    var progressBar: ProgressBar? = null

    protected var hivIndexList: MutableList<HivIndexObject?> = arrayListOf()

    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiv_client_index_list)
        val extras = intent.extras
        if (extras != null) {
            memberObject =
                intent.getSerializableExtra(Constants.HivMemberObject.MEMBER_OBJECT) as HivMemberObject
        }

        setUpView()
        setUpActionBar()
        initializePresenter()
    }

    override fun onCreation() {
        Timber.v("Empty onCreation")
    }

    override fun onResumption() {
        Timber.v("Empty onResumption")
    }

    private fun setUpActionBar() {
        val toolbar =
            findViewById<Toolbar>(R.id.collapsing_toolbar)
        tvTitle =
            toolbar.findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            val upArrow =
                resources.getDrawable(R.drawable.ic_arrow_back_white_24dp)
            upArrow.setColorFilter(
                resources.getColor(R.color.text_blue),
                PorterDuff.Mode.SRC_ATOP
            )
            actionBar.setHomeAsUpIndicator(upArrow)
        }
        toolbar.setNavigationOnClickListener { finish() }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager

        mAdapter = IndexClientsAdapter(this, hivIndexList)
        recyclerView!!.adapter = mAdapter
    }

    private fun setUpView() {
        recyclerView =
            findViewById(R.id.recyclerView)
        progressBar =
            findViewById(R.id.progressBarIndexList)
        tvTitle =
            findViewById(R.id.tvTitle)

        with(tvTitle) {
            this!!.text = getString(
                R.string.back_to,
                "${memberObject!!.firstName} ${memberObject!!.middleName} ${memberObject!!.lastName}"
            )
        }

        Timber.d("before initializing presenter")
        setUpPresenter()
    }

    fun setUpPresenter() {
        Timber.d("initializing presenter")
        Timber.d("Base Entity Id = "+memberObject!!.baseEntityId)

        presenter = memberObject?.baseEntityId?.let { it1 ->
            Timber.d("Base Entity Id = "+memberObject!!.baseEntityId)
            BaseHivClientIndexListPresenter(
                it1,
                BaseHivClientIndexListInteractor(),
                this
            )
        }
        presenter?.initialize();
    }


    override val viewContext: Context
        get() = applicationContext

    override fun displayLoadingState(state: Boolean) {
        progressBar!!.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun refreshIndexList(hivIndexObjects: List<HivIndexObject?>) {
        hivIndexList.clear()
        hivIndexList.addAll(hivIndexObjects)
        mAdapter!!.notifyDataSetChanged()
        recyclerView!!.adapter = mAdapter
    }

    companion object {
        fun startMe(activity: Activity, memberObject: MemberObject?) {
            val intent = Intent(activity, BaseHivClientIndexListActivity::class.java)
            intent.putExtra(ANC_MEMBER_OBJECTS.MEMBER_PROFILE_OBJECT, memberObject)
            activity.startActivity(intent)
        }
    }
}