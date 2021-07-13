package com.example.nureca.data.model

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmField
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class CountryRealm(
    var countryName : String? = "",
    var ISO2 : String? = "",
    var slug : String? = "",
    var _partition: String = "public"
) : RealmObject(){

    @PrimaryKey
    var _id: ObjectId = ObjectId()


}
