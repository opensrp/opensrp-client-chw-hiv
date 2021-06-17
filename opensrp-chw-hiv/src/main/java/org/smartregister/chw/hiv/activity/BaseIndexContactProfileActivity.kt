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
import androidx.viewpager.widget.ViewPager
import de.hdodenhof.circleimageview.CircleImageView
import org.apache.commons.lang3.StringUtils
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Period
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseIndexContactProfileContract
import org.smartregister.chw.hiv.custom_views.BaseIndexFloatingMenu
import org.smartregister.chw.hiv.dao.HivDao
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.chw.hiv.interactor.BaseIndexContactProfileInteractor
import org.smartregister.chw.hiv.presenter.BaseIndexContactProfilePresenter
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.HivUtil.fromHtml
import org.smartregister.chw.hiv.util.HivUtil.getMemberProfileImageResourceIDentifier
import org.smartregister.domain.AlertStatus
import org.smartregister.helper.ImageRenderHelper
import org.smartregister.view.activity.BaseProfileActivity
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

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
    private var rlUpcomingServices: RelativeLayout? = null
    private var rlFamilyServicesDue: RelativeLayout? = null
    private var rlIndexClients: RelativeLayout? = null
    private var tvLastVisitDay: TextView? = null
    private var tvViewMedicalHistory: TextView? = null
    private var tvUpComingServices: TextView? = null
    private var tvFamilyStatus: TextView? = null
    private var tvFamilyProfile: TextView? = null
    private var tvRecordHivFollowUp: TextView? = null
    private var tvHivRow: TextView? = null
    var hivContactProfilePresenter: BaseIndexContactProfileContract.Presenter? = null
    var hivFloatingMenu: BaseIndexFloatingMenu? = null
    var hivIndexContactObject: HivIndexContactObject? = null
    private var numOfDays = 0
    private var progressBar: ProgressBar? = null
    private var profileImageView: CircleImageView? = null
    private var tvName: TextView? = null
    private var tvGender: TextView? = null
    private var tvLocation: TextView? = null
    private var tvUniqueID: TextView? = null
    private var overDueRow: View? = null
    private var familyRow: View? = null
    override fun onCreation() {
        setContentView(R.layout.activity_base_hiv_profile)
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
        toolbar.setNavigationOnClickListener { v: View? -> finish() }
        appBarLayout = findViewById(R.id.collapsing_toolbar_appbarlayout)
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.outlineProvider = null
        }
        hivIndexContactObject =
            intent.getSerializableExtra(Constants.ActivityPayload.MEMBER_OBJECT) as HivIndexContactObject
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
        tvUpComingServices = findViewById(R.id.textview_name_due)
        tvFamilyStatus = findViewById(R.id.textview_family_has)
        tvFamilyProfile = findViewById(R.id.text_view_family_profile)
        tvHivRow = findViewById(R.id.textview_hiv_registration_date_row)
        rlLastVisitLayout = findViewById(R.id.rl_last_visit_layout)
        tvLastVisitDay = findViewById(R.id.textview_last_vist_day)
        tvViewMedicalHistory = findViewById(R.id.textview_medical_history)
        rlUpcomingServices = findViewById(R.id.rlUpcomingServices)
        rlFamilyServicesDue = findViewById(R.id.rlFamilyServicesDue)
        rlIndexClients = findViewById(R.id.rlIndexClients)
        progressBar = findViewById(R.id.progress_bar)
        tickImage = findViewById(R.id.tick_image)
        tvVisitDone = findViewById(R.id.textview_visit_done)
        tvEditVisit = findViewById(R.id.textview_edit)
        tvUndo = findViewById(R.id.textview_undo)
        profileImageView =
            findViewById(R.id.imageview_profile)
        tvRecordHivFollowUp = findViewById(R.id.textview_record_reccuring_visit)
        tvUndo?.let { tvUndo?.setOnClickListener(this) }
        tvEditVisit?.let { tvEditVisit?.setOnClickListener(this) }
        tvRecordHivFollowUp?.let { tvRecordHivFollowUp?.setOnClickListener(this) }
        findViewById<View>(R.id.rl_last_visit_layout).setOnClickListener(this)
        findViewById<View>(R.id.rlUpcomingServices).setOnClickListener(this)
        findViewById<View>(R.id.rlFamilyServicesDue).setOnClickListener(this)
        findViewById<View>(R.id.rlIndexClients).setOnClickListener(this)
        findViewById<View>(R.id.rlHivRegistrationDate).setOnClickListener(this)
    }

    override fun initializePresenter() {
        hivContactProfilePresenter =
            BaseIndexContactProfilePresenter(this, BaseIndexContactProfileInteractor(), hivIndexContactObject!!)
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
        hivContactProfilePresenter!!.refreshProfileHivStatusInfo()
    }

    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.title_layout -> {
                onBackPressed()
            }
            R.id.rlFamilyServicesDue -> {
                openFamilyDueServices()
            }
            R.id.textview_record_reccuring_visit -> {
                openFollowUpVisitForm(false)
            }
            R.id.textview_edit -> {
                openFollowUpVisitForm(true)
            }
        }
    }

    override fun setupFollowupVisitEditViews(isWithin24Hours: Boolean) {
        if (isWithin24Hours) {
            recordFollowUpVisitLayout!!.visibility = View.GONE
//            recordVisitStatusBarLayout.setVisibility(View.VISIBLE);
//            tvEditVisit.setVisibility(View.VISIBLE);
        } else {
            tvEditVisit!!.visibility = View.GONE
            recordFollowUpVisitLayout!!.visibility = View.VISIBLE
            recordVisitStatusBarLayout!!.visibility = View.GONE
        }
    }

    override val context: Context
        get() = this

    override fun openFamilyDueServices() {
        // TODO :: Show family due services
    }

    override fun openFollowUpVisitForm(isEdit: Boolean) {
        // TODO :: Open follow-up visit form for editing
    }

    override fun setUpComingServicesStatus(
        service: String?,
        status: AlertStatus?,
        date: Date?
    ) {
        showProgressBar(false)
        val dateFormat =
            SimpleDateFormat("dd MMM", Locale.getDefault())
        if (status == AlertStatus.complete) return
        overDueRow!!.visibility = View.VISIBLE
        rlUpcomingServices!!.visibility = View.VISIBLE
        if (status == AlertStatus.upcoming) {
            tvUpComingServices!!.text = fromHtml(
                getString(
                    R.string.hiv_upcoming_visit,
                    service,
                    dateFormat.format(date)
                )
            )
        } else {
            tvUpComingServices!!.text = fromHtml(
                getString(
                    R.string.hiv_service_due,
                    service,
                    dateFormat.format(date)
                )
            )
        }
    }


    override fun setFamilyStatus(status: AlertStatus?) {
        findViewById<View>(R.id.rlHivRegistrationDate).visibility = View.VISIBLE
        familyRow!!.visibility = View.VISIBLE
        rlFamilyServicesDue!!.visibility = View.VISIBLE

        when (status) {
            AlertStatus.complete -> {
                tvFamilyStatus!!.text = getString(R.string.client_has_nothing_due)
            }
            AlertStatus.normal -> {
                tvFamilyStatus!!.text = getString(R.string.client_has_services_due)
            }
            AlertStatus.urgent -> {
                tvFamilyStatus!!.text =
                    fromHtml(getString(R.string.client_has_service_overdue))
            }
            else -> {
                tvFamilyStatus!!.text = getString(R.string.client_has_nothing_due)
            }
        }
        tvFamilyProfile!!.text = getString(R.string.go_to_client_s_profile)
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
        tvHivRow!!.text = String.format(
            getString(R.string.hiv_client_registered_text),
            getString(R.string.hiv_on),
            hivIndexContactObject.hivIndexRegistrationDate
        )


        hivIndexContactObject.hivClientId?.let {
            val hivMemberObject = HivDao.getMember(it)

            val tvIndexNameTitle = findViewById<View>(R.id.hiv_family_head) as TextView
            tvIndexNameTitle.text = getString(R.string.associated_index_client)

            val tvIndexName = findViewById<View>(R.id.hiv_primary_caregiver) as TextView
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

    private fun formatTime(dateTime: String): CharSequence? {
        var timePassedString: CharSequence? = null
        try {
            val df =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = df.parse(dateTime)
            timePassedString =
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(date)
        } catch (e: Exception) {
            Timber.d(e)
        }
        return timePassedString
    }

    override fun updateLastVisitRow(lastVisitDate: Date?) {
        showProgressBar(false)
        if (lastVisitDate == null) return
        tvLastVisitDay!!.visibility = View.VISIBLE
        numOfDays = Days.daysBetween(
            DateTime(lastVisitDate).toLocalDate(),
            DateTime().toLocalDate()
        ).days
        tvLastVisitDay!!.text = getString(
            R.string.last_visit_n_days_ago,
            if (numOfDays <= 1) getString(R.string.less_than_twenty_four) else "$numOfDays " + getString(
                R.string.days
            )
        )
        rlLastVisitLayout!!.visibility = View.GONE
        lastVisitRow!!.visibility = View.GONE
    }

    override fun onMemberDetailsReloaded(hivIndexContactObject: HivIndexContactObject?) {
        setupViews()
        fetchProfileData()
    }

    override fun setFollowUpButtonDue() {
        showFollowUpVisitButton(true)
        tvRecordHivFollowUp!!.background = resources.getDrawable(R.drawable.record_hiv_followup)
    }

    override fun setFollowUpButtonOverdue() {
        showFollowUpVisitButton(true)
        tvRecordHivFollowUp!!.background =
            resources.getDrawable(R.drawable.record_hiv_followup_overdue)
    }

    override fun showFollowUpVisitButton(status: Boolean) {
        if (status) tvRecordHivFollowUp!!.visibility =
            View.VISIBLE else tvRecordHivFollowUp!!.visibility = View.GONE
    }

    override fun hideFollowUpVisitButton() {
        tvRecordHivFollowUp!!.visibility = View.GONE
    }

    override fun showProgressBar(status: Boolean) {
        progressBar!!.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun openHivRegistrationForm() {}

    companion object {
        fun startProfileActivity(activity: Activity, hivIndexContactObject: HivIndexContactObject) {
            val intent = Intent(activity, BaseIndexContactProfileActivity::class.java)
            intent.putExtra(Constants.ActivityPayload.MEMBER_OBJECT, hivIndexContactObject)
            activity.startActivity(intent)
        }
    }
}