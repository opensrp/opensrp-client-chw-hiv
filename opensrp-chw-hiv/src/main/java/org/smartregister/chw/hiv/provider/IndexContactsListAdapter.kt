package org.smartregister.chw.hiv.provider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

/**
 * Created by cozej4 on 2021-07-13.
 *
 * @cozej4 https://github.com/cozej4
 *
 * This is the adapter of the Index Contacts List RecyclerView used to populate a list of elicited
 * index contacts of an hiv client
 */
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
            val patientName = Utils.getName(firstName, client.lastName!!)

            holder.patientName.text = kotlin.String.format(
                    java.util.Locale.getDefault(), "%s, %d", patientName, age
            )

            if (client.gender!!.toLowerCase() == "male") {
                holder.textViewGender.text = context.getString(R.string.sex_male)
            } else {
                holder.textViewGender.text = context.getString(R.string.sex_female)
            }

            val village = client.address
            val testResult = client.testResults
            when {
                village?.isNotEmpty()!! -> {
                    holder.textViewVillage.text = java.text.MessageFormat.format(
                            context.getString(org.smartregister.chw.hiv.R.string.separator),
                            village
                    )
                }
            }

            when{
                client.relationship?.isNotEmpty()!! ->{
                    if(client.relationship.equals("sexual_partner")){
                        holder.relationship.text = context.getString(R.string.index_contact_sexual_partner)
                    }else if(client.relationship.equals("needle_sharing_partner")){
                        holder.relationship.text = context.getString(R.string.index_contact_needle_sharing_partner)
                    }else if(client.relationship.equals("biological_mother")){
                        holder.relationship.text = context.getString(R.string.index_contact_biological_mother)
                    }else if(client.relationship.equals("biological_father")){
                        holder.relationship.text = context.getString(R.string.index_contact_biological_father)
                    }else if(client.relationship.equals("biological_child_under_15")){
                        holder.relationship.text = context.getString(R.string.index_contact_biological_child_under_15)
                    }else if(client.relationship.equals("siblings_under_15")){
                        holder.relationship.text = context.getString(R.string.index_contact_siblings_under_15)
                    }else{
                        holder.relationship.visibility = View.GONE
                    }
                }
            }
            when {
                testResult?.isNotEmpty()!! -> {
                    if (testResult.toLowerCase(Locale.ROOT) == "positive") {
                        holder.hivStatus.text = java.text.MessageFormat.format(
                                context.getString(R.string.separator),
                                context.getString(R.string.hiv_positive_status)
                        )
                        holder.hivStatus.setTextColor(context.resources.getColor(R.color.colorRed))
                    } else {
                        holder.hivStatus.text = java.text.MessageFormat.format(
                                context.getString(R.string.separator),
                                context.getString(R.string.hiv_negative_status)
                        )
                        holder.hivStatus.setTextColor(context.resources.getColor(R.color.accent))
                    }
                }
                client.hivStatus?.isNotEmpty()!! -> {
                    if (client.hivStatus!!.toLowerCase(Locale.ROOT) == "positive") {
                        holder.hivStatus.text = java.text.MessageFormat.format(
                                context.getString(R.string.separator),
                                context.getString(R.string.hiv_positive_status)
                        )
                        holder.hivStatus.setTextColor(context.resources.getColor(R.color.colorRed))
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

    /**
     * The implementation of the view holder
     */
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var patientName: TextView
        var textViewVillage: TextView
        var textViewGender: TextView
        var view: View
        var hivStatus: TextView
        var relationship: TextView

        init {
            patientName = view.findViewById(R.id.patient_name_age)
            textViewVillage = view.findViewById(R.id.text_view_village)
            textViewGender = view.findViewById(R.id.text_view_gender)
            hivStatus = view.findViewById(R.id.hiv_status)
            relationship = view.findViewById(R.id.relationship)
            this.view = view
        }
    }
}