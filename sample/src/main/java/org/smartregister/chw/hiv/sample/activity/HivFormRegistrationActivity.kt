package org.smartregister.chw.hiv.sample.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import org.smartregister.chw.hiv.R

class HivFormRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.neat_hiv_registration_form_activity)
        JsonFormBuilder(
            this, "json.form/hiv_registration_form.json",
            findViewById<LinearLayout>(R.id.formLayout)
        ).buildForm(null, null)
    }
}