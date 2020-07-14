package org.smartregister.chw.hiv.sample.activity

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormEmbedded
import org.smartregister.chw.hiv.R

class HivFormRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.neat_hiv_registration_form_activity)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.sampleToolBar)
        setSupportActionBar(toolbar)
        val jsonFormBuilder = JsonFormBuilder(this, "json.form/hiv_followup_visit.json")
        JsonFormEmbedded(jsonFormBuilder, findViewById<LinearLayout>(R.id.formLayout)).buildForm()
    }
}