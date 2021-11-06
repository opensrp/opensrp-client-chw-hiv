package org.smartregister.chw.hiv.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.viewpager.widget.ViewPager
import de.hdodenhof.circleimageview.CircleImageView
import org.apache.commons.lang3.StringUtils
import org.joda.time.DateTime
import org.joda.time.Period
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseIndexContactProfileContract
import org.smartregister.chw.hiv.custom_views.BaseIndexFloatingMenu
import org.smartregister.chw.hiv.dao.HivDao
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.chw.hiv.interactor.BaseIndexContactProfileInteractor
import org.smartregister.chw.hiv.presenter.BaseIndexContactProfilePresenter
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.HivUtil.getMemberProfileImageResourceIDentifier
import org.smartregister.helper.ImageRenderHelper
import org.smartregister.view.activity.BaseProfileActivity
import java.util.*

/**
 * Created by cozej4 on 2021-07-13.
 *
 * @cozej4 https://github.com/cozej4
 *
 * This is the elicited index contact profile activity.
 */
open class BaseIndexContactProfileActivity : BaseProfileActivity(),
    BaseIndexContactProfileContract.View {
    private var lastVisitRow: View? = null
    private var recordFollowUpVisitLayout: LinearLayout? = null
    private var recordVisitStatusBarLayout: RelativeLayout? = null
    private var tickImage: ImageView? = null
    private var tvEditVisit: TextView? = null
    private var tvUndo: TextView? = null
    private var tvVisitDone: TextView? = null
    private var rlLastVisitLayout: RelativeLayout? = null
    private var rlIndexClients: RelativeLayout? = null
    private var tvRecordHivFollowUp: TextView? = null
    private var tvHivRow: TextView? = null
    var hivContactProfilePresenter: BaseIndexContactProfileContract.Presenter? = null
    var hivFloatingMenu: BaseIndexFloatingMenu? = null
    var hivIndexContactObject: HivIndexContactObject? = null
    private var progressBar: ProgressBar? = null
    private var profileImageView: CircleImageView? = null
    private var tvName: TextView? = null
    private var tvGender: TextView? = null
    private var tvLocation: TextView? = null
    private var tvUniqueID: TextView? = null
    private var testResults: TextView? = null
    private var overDueRow: View? = null
    private var familyRow: View? = null
    override fun onCreation() {
        setContentView(R.layout.activity_base_hiv_index_contact_profile)
        val toolbar =
            findViewById<Toolbar>(R.id.collapsing_toolbar)
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
        toolbar.setNavigationOnClickListener {
            NavUtils.navigateUpFromSameTask(this)
        }
        appBarLayout = findViewById(R.id.collapsing_toolbar_appbarlayout)
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.outlineProvider = null
        }
        hivIndexContactObject =
            intent.getSerializableExtra(Constants.ActivityPayload.HIV_MEMBER_OBJECT) as HivIndexContactObject
        setupViews()
        initializePresenter()
        fetchProfileData()
        initializeCallFAB()
    }

    override fun setupViews() {
        imageRenderHelper = ImageRenderHelper(this)
        tvName = findViewById(R.id.textview_name)
        tvGender = findViewById(R.id.textview_gender)
        tvLocation = findViewById(R.id.textview_address)
        tvUniqueID = findViewById(R.id.textview_unique_id)
        recordVisitStatusBarLayout =
            findViewById(R.id.record_visit_status_bar_layout)
        recordFollowUpVisitLayout = findViewById(R.id.record_recurring_layout)
        lastVisitRow = findViewById(R.id.view_last_visit_row)
        overDueRow = findViewById(R.id.view_most_due_overdue_row)
        familyRow = findViewById(R.id.view_family_row)
        tvHivRow = findViewById(R.id.textview_hiv_registration_date_row)
        rlLastVisitLayout = findViewById(R.id.rl_last_visit_layout)
        rlIndexClients = findViewById(R.id.rlIndexClients)
        progressBar = findViewById(R.id.progress_bar)
        tickImage = findViewById(R.id.tick_image)
        tvVisitDone = findViewById(R.id.textview_visit_done)
        tvEditVisit = findViewById(R.id.textview_edit)
        tvUndo = findViewById(R.id.textview_undo)
        testResults = findViewById(R.id.test_results)
        profileImageView =
            findViewById(R.id.imageview_profile)
        tvRecordHivFollowUp = findViewById(R.id.textview_record_reccuring_visit)
        tvUndo?.let { tvUndo?.setOnClickListener(this) }
        tvEditVisit?.let { tvEditVisit?.setOnClickListener(this) }
        tvRecordHivFollowUp?.let { tvRecordHivFollowUp?.setOnClickListener(this) }
    }

    override fun initializePresenter() {
        hivContactProfilePresenter =
            BaseIndexContactProfilePresenter(
                this,
                BaseIndexContactProfileInteractor(),
                hivIndexContactObject!!
            )
    }

    open fun initializeCallFAB() {
        if (StringUtils.isNotBlank(hivIndexContactObject!!.phoneNumber)
            || StringUtils.isNotBlank(hivIndexContactObject!!.familyHeadPhoneNumber)
        ) {
            hivFloatingMenu = BaseIndexFloatingMenu(this, hivIndexContactObject!!)
            hivFloatingMenu!!.gravity = Gravity.BOTTOM or Gravity.END
            val linearLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            addContentView(hivFloatingMenu, linearLayoutParams)
        }
    }

    override fun setupViewPager(viewPager: ViewPager): ViewPager? {
        return null
    }

    override fun fetchProfileData() {
        hivContactProfilePresenter!!.refreshProfileData()
    }

    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.title_layout -> {
                onBackPressed()
            }
            R.id.textview_record_reccuring_visit -> {
                openFollowUpVisitForm(false)
            }
            R.id.textview_edit -> {
                openFollowUpVisitForm(true)
            }
        }
    }

    override fun setupFollowupVisitEditViews(visitDone: Boolean) {
        if (visitDone) {
            recordFollowUpVisitLayout!!.visibility = View.GONE
            recordVisitStatusBarLayout!!.visibility = View.VISIBLE
        } else {
            recordVisitStatusBarLayout!!.visibility = View.GONE
            recordFollowUpVisitLayout!!.visibility = View.VISIBLE
        }
    }

    override val context: Context
        get() = this

    override fun openFollowUpVisitForm(isEdit: Boolean) {
        // TODO :: Open follow-up visit form for editing
    }

    override fun setProfileViewDetails(hivIndexContactObject: HivIndexContactObject?) {
        val age = Period(
            DateTime(hivIndexContactObject!!.dob),
            DateTime()
        ).years
        tvName!!.text = String.format(
            Locale.getDefault(), "%s %s %s, %d", hivIndexContactObject.firstName,
            hivIndexContactObject.middleName, hivIndexContactObject.lastName, age
        )
        tvGender!!.text = hivIndexContactObject.gender
        tvLocation!!.text = hivIndexContactObject.address
        tvUniqueID!!.text = hivIndexContactObject.uniqueId
        imageRenderHelper.refreshProfileImage(
            hivIndexContactObject.baseEntityId,
            profileImageView,
            getMemberProfileImageResourceIDentifier()
        )

        if (hivIndexContactObject.hivStatus.equals("positive", ignoreCase = true) && hivIndexContactObject.hasStartedMediation!!) {
            testResults!!.text = resources.getString(R.string.hiv_positive_status_on_medication)
            testResults!!.setTextColor(context.resources.getColor(R.color.colorRed))
        } else if (hivIndexContactObject.hasTheContactClientBeenTested.equals(
                "yes",
                ignoreCase = true
            )
        ) {
            testResults!!.visibility = View.VISIBLE
            if (hivIndexContactObject.testResults.equals("positive", ignoreCase = true)) {
                testResults!!.text = resources.getString(R.string.hiv_positive_status)
                testResults!!.setTextColor(context.resources.getColor(R.color.colorRed))
            } else {
                testResults!!.text = resources.getString(R.string.hiv_negative_status)
                testResults!!.setTextColor(context.resources.getColor(R.color.accent))
            }
        } else if (hivIndexContactObject.hasTheContactClientBeenTested != "") {
            testResults!!.visibility = View.VISIBLE
            testResults!!.text = resources.getString(R.string.client_was_not_tested)
            testResults!!.setTextColor(context.resources.getColor(R.color.colorRed))
        } else {
            testResults!!.visibility = View.GONE
        }

        hivIndexContactObject.hivClientId?.let {
            val hivMemberObject = HivDao.getMember(it)

            val tvIndexNameTitle = findViewById<View>(R.id.associated_hiv_index_title) as TextView

            val tvIndexName = findViewById<View>(R.id.associated_hiv_index_name) as TextView
            if (hivMemberObject != null) {
                tvIndexNameTitle.visibility = View.VISIBLE
                tvIndexName.visibility = View.VISIBLE

                tvIndexName.text = String.format(
                    Locale.getDefault(), "%s %s %s, %d", hivMemberObject.firstName,
                    hivMemberObject.middleName, hivMemberObject.lastName, age
                )
            }
        }


    }

    override fun onMemberDetailsReloaded(hivIndexContactObject: HivIndexContactObject?) {
        setupViews()
        fetchProfileData()
    }

    override fun setFollowUpButtonDue() {
        showFollowUpVisitButton(true)
        tvRecordHivFollowUp!!.background = resources.getDrawable(R.drawable.record_hiv_followup)
    }

    override fun checkFollowupStatus() {
        //Implement this
    }

    override fun setFollowUpButtonOverdue() {
        showFollowUpVisitButton(true)
        tvRecordHivFollowUp!!.background =
            resources.getDrawable(R.drawable.record_hiv_followup_overdue)
    }

    override fun showFollowUpVisitButton(status: Boolean) {
        if (status)
            tvRecordHivFollowUp!!.visibility =
                View.VISIBLE else tvRecordHivFollowUp!!.visibility = View.GONE
    }

    override fun hideFollowUpVisitButton() {
        tvRecordHivFollowUp!!.visibility = View.GONE
    }

    override fun showProgressBar(status: Boolean) {
        progressBar!!.visibility = if (status) View.VISIBLE else View.GONE
    }

    companion object {
        fun startProfileActivity(activity: Activity, hivIndexContactObject: HivIndexContactObject) {
            val intent = Intent(activity, BaseIndexContactProfileActivity::class.java)
            intent.putExtra(Constants.ActivityPayload.HIV_MEMBER_OBJECT, hivIndexContactObject)
            activity.startActivity(intent)
        }
    }
}