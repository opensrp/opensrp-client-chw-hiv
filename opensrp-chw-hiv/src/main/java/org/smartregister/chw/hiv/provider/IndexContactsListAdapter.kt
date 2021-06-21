package org.smartregister.chw.hiv.provider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.joda.time.DateTime
import org.joda.time.Period
import org.smartregister.chw.hiv.R
import org.smartregister.chw.hiv.contract.BaseIndexClientsContactListContract
import org.smartregister.chw.hiv.domain.HivIndexContactObject
import org.smartregister.util.Utils
import timber.log.Timber
import java.util.*

class IndexContactsListAdapter(
    private val context: Context,
    private val indexContactClientsList: List<HivIndexContactObject?>
) : RecyclerView.Adapter<IndexContactsListAdapter.MyViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(viewGroup.context)
        val v = layoutInflater!!.inflate(R.layout.hiv_index_contact_list_row_item, viewGroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        val client = indexContactClientsList[i]

        try {
            val age = Period(DateTime(client?.dob), DateTime()).years

            val firstName = Utils.getName(client?.firstName!!, client.middleName!!)
            val patientName = Utils.getName(
                firstName, client.lastName!!
            )

            with(holder) {
                this.patientName.text = kotlin.String.format(
                    java.util.Locale.getDefault(), "%s, %d", patientName, age
                )
                textViewGender.text = client?.gender ?: ""
                val village = client?.address
                val testResult = client?.testResults
                when {
                    village?.isNotEmpty()!! -> {
                        textViewVillage.text = java.text.MessageFormat.format(
                            context.getString(org.smartregister.chw.hiv.R.string.separator),
                            village
                        )
                    }
                }

                when {
                    testResult?.isNotEmpty()!! -> {
                        if (testResult.toLowerCase(Locale.ROOT) == "positive") {
                            hivStatus.text = java.text.MessageFormat.format(
                                context.getString(R.string.separator),
                                context.getString(R.string.hiv_positive_status)
                            )
                            hivStatus.setTextColor(context.resources.getColor(R.color.colorRed))
                        } else {
                            hivStatus.text = java.text.MessageFormat.format(
                                context.getString(R.string.separator),
                                context.getString(R.string.hiv_negative_status)
                            )
                            hivStatus.setTextColor(context.resources.getColor(R.color.accent))
                        }

                    }
                }


            }


            holder.view.setOnClickListener(View.OnClickListener {
                (context as BaseIndexClientsContactListContract.View).presenter!!.openIndexContactProfile(
                    indexContactClientsList[i]
                )
            })
        } catch (e: IllegalStateException) {
            Timber.e(e)
        }
    }

    override fun getItemCount(): Int {
        return indexContactClientsList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var patientName: TextView
        var textViewVillage: TextView
        var textViewGender: TextView
        var linearLayoutTitle: LinearLayout? = null
        var linearLayoutSubTitles: LinearLayout? = null
        var view: View
        var patientColumn: View? = null
        var hivStatus: TextView

        init {
            patientName = view.findViewById(R.id.patient_name_age)
            textViewVillage = view.findViewById(R.id.text_view_village)
            textViewGender = view.findViewById(R.id.text_view_gender)
            hivStatus = view.findViewById(R.id.hiv_status)
            this.view = view
        }
    }
}