package org.smartregister.chw.hiv.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by cozej4 on 2019-12-02.
 * This entity class is used for holding data for neat form view options especially for MultiChoiceCheckBox, RadioGroup
 * and Spinner views
 *
 * @author cozej4 https://github.com/cozej4
 */
class NeatFormOption : Serializable {

    @JvmField
    var name: String? = null

    @JvmField
    var text: String? = null

    @JvmField
    @SerializedName("meta_data")
    var neatFormMetaData: NeatFormMetaData? = null
}