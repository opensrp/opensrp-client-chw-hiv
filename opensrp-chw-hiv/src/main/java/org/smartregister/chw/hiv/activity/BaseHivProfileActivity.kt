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
import org.joda.time.Days
import org.joda.time.Period
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseHivProfileContract
import org.smartregister.chw.hiv.custom_views.BaseHivFloatingMenu
import org.smartregister.chw.hiv.domain.HivMemberObject
import org.smartregister.chw.hiv.interactor.BaseHivProfileInteractor
import org.smartregister.chw.hiv.presenter.BaseHivProfilePresenter
import org.smartregister.chw.hiv.util.Constants
import org.smartregister.chw.hiv.util.HivUtil.fromHtml
import org.smartregister.chw.hiv.util.HivUtil.getMemberProfileImageResourceIDentifier
import org.smartregister.domain.AlertStatus
import org.smartregister.helper.ImageRenderHelper
import org.smartregister.view.activity.BaseProfileActivity
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

open class BaseHivProfileActivity : BaseProfileActivity(),
        BaseHivProfileContract.View {
    private var lastVisitRow: View? = null
    private var recordFollowUpVisitLayout: LinearLayout? = null
    protected var recordIndexContactLayout: LinearLayout? = null
    private var recordVisitStatusBarLayout: RelativeLayout? = null
    private var tickImage: ImageView? = null
    private var tvEditVisit: TextView? = null
    private var tvUndo: TextView? = null
    private var tvVisitDone: TextView? = null
    private var rlLastVisitLayout: RelativeLayout? = null
    protected var rlUpcomingServices: RelativeLayout? = null
    private var rlFamilyServicesDue: RelativeLayout? = null
    private var rlIndexClients: RelativeLayout? = null
    private var tvLastVisitDay: TextView? = null
    private var tvViewMedicalHistory: TextView? = null
    private var tvUpComingServices: TextView? = null
    private var tvFamilyStatus: TextView? = null
    private var tvFamilyProfile: TextView? = null
    private var tvRecordHivFollowUp: TextView? = null
    private var tvHivRow: TextView? = null
    var hivProfilePresenter: BaseHivProfileContract.Presenter? = null
    var hivFloatingMenu: BaseHivFloatingMenu? = null
    var hivMemberObject: HivMemberObject? = null
    private var numOfDays = 0
    private var progressBar: ProgressBar? = null
    private var profileImageView: CircleImageView? = null
    private var tvName: TextView? = null
    private var tvGender: TextView? = null
    private var tvLocation: TextView? = null
    private var tvUniqueID: TextView? = null
    protected var tvCbhsNumber: TextView? = null
    protected var tvStatus: TextView? = null
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
        toolbar.setNavigationOnClickListener { NavUtils.navigateUpFromSameTask(this) }
        appBarLayout = findViewById(R.id.collapsing_toolbar_appbarlayout)
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.outlineProvider = null
        }
        hivMemberObject =
                intent.getSerializableExtra(Constants.ActivityPayload.HIV_MEMBER_OBJECT) as HivMemberObject
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
        tvStatus = findViewById(R.id.textview_status)
        tvCbhsNumber = findViewById(R.id.textview_cbhs_number)
        recordVisitStatusBarLayout =
                findViewById(R.id.record_visit_status_bar_layout)
        recordFollowUpVisitLayout = findViewById(R.id.record_recurring_layout)
        recordIndexContactLayout = findViewById(R.id.record_index_contact_layout)
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
        findViewById<View>(R.id.textview_record_index_contact_visit).setOnClickListener(this)
    }

    override fun initializePresenter() {
        hivProfilePresenter =
                BaseHivProfilePresenter(this, BaseHivProfileInteractor(), hivMemberObject!!)
    }

    open fun initializeCallFAB() {
        if (StringUtils.isNotBlank(hivMemberObject!!.phoneNumber)
                || StringUtils.isNotBlank(hivMemberObject!!.familyHeadPhoneNumber)
        ) {
            hivFloatingMenu = BaseHivFloatingMenu(this, hivMemberObject!!)
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
        hivProfilePresenter!!.refreshProfileData()
        hivProfilePresenter!!.refreshProfileHivStatusInfo()
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.title_layout) {
            onBackPressed()
        } else if (id == R.id.rl_last_visit_layout) {
            openMedicalHistory()
        } else if (id == R.id.rlUpcomingServices) {
            openUpcomingServices()
        } else if (id == R.id.rlFamilyServicesDue) {
            openFamilyDueServices()
        } else if (id == R.id.rlIndexClients) {
            openIndexClientsList(hivMemberObject)
        } else if (id == R.id.textview_record_reccuring_visit) {
            openFollowUpVisitForm(false)
        } else if (id == R.id.textview_edit) {
            openFollowUpVisitForm(true)
        } else if (id == R.id.textview_record_index_contact_visit) {
            openIndexContactRegistration()
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

    override fun openMedicalHistory() {
        // TODO :: Open medical history view
    }

    override fun openUpcomingServices() {
        // TODO :: Show upcoming services
    }

    override fun openFamilyDueServices() {
        // TODO :: Show family due services
    }

    override fun openIndexClientsList(hivMemberObject: HivMemberObject?) {
        // TODO :: Open Index List
    }

    override fun openFollowUpVisitForm(isEdit: Boolean) {
        // TODO :: Open follow-up visit form for editing
    }

    override fun openIndexContactRegistration() {
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

        when {
            hivMemberObject?.familyMemberEntityType.equals(Constants.FamilyMemberEntityType.EC_INDEPENDENT_CLIENT) -> {
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
            else -> {
                when (status) {
                    AlertStatus.complete -> {
                        tvFamilyStatus!!.text = getString(R.string.family_has_nothing_due)
                    }
                    AlertStatus.normal -> {
                        tvFamilyStatus!!.text = getString(R.string.family_has_services_due)
                    }
                    AlertStatus.urgent -> {
                        tvFamilyStatus!!.text =
                                fromHtml(getString(R.string.family_has_service_overdue))
                    }
                    else -> {
                        tvFamilyStatus!!.text = getString(R.string.family_has_nothing_due)
                    }
                }
                tvFamilyProfile!!.text = getString(R.string.go_to_family_s_profile)

            }
        }
    }

    override fun setProfileViewDetails(hivMemberObject: HivMemberObject?) {
        setupViews()
        val age = Period(
                DateTime(hivMemberObject!!.age),
                DateTime()
        ).years
        tvName!!.text = String.format(
                Locale.getDefault(), "%s %s %s, %d", hivMemberObject.firstName,
                hivMemberObject.middleName, hivMemberObject.lastName, age
        )
        tvGender!!.text = hivMemberObject.gender
        tvLocation!!.text = hivMemberObject.address
        tvUniqueID!!.text = hivMemberObject.uniqueId

        if (!hivMemberObject.cbhsNumber.isNullOrEmpty())
            tvCbhsNumber!!.text = "CBHS:" + hivMemberObject.cbhsNumber

        imageRenderHelper.refreshProfileImage(
                hivMemberObject.baseEntityId,
                profileImageView,
                getMemberProfileImageResourceIDentifier()
        )
        tvHivRow!!.text = String.format(
                getString(R.string.hiv_client_registered_text),
                getString(R.string.hiv_on),
                hivMemberObject.hivRegistrationDate
        )
        if (StringUtils.isNotBlank(hivMemberObject.familyHead) && hivMemberObject.familyHead == hivMemberObject.baseEntityId) {
            findViewById<View>(R.id.hiv_family_head).visibility = View.VISIBLE
        }
        if (StringUtils.isNotBlank(hivMemberObject.primaryCareGiver) && hivMemberObject.primaryCareGiver == hivMemberObject.baseEntityId) {
            findViewById<View>(R.id.hiv_primary_caregiver).visibility = View.VISIBLE
        }


    }

    override fun setIndexClientsStatus(boolean: Boolean) {
        if (boolean) {
            rlIndexClients?.visibility = View.VISIBLE
        } else {
            rlIndexClients?.visibility = View.GONE
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

    override fun onMemberDetailsReloaded(hivMemberObject: HivMemberObject?) {
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
        fun startProfileActivity(activity: Activity, memberObject: HivMemberObject) {
            val intent = Intent(activity, BaseHivProfileActivity::class.java)
            intent.putExtra(Constants.ActivityPayload.HIV_MEMBER_OBJECT, memberObject)
            activity.startActivity(intent)
        }
    }
}