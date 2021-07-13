package com.example.nureca.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmField
import org.bson.types.ObjectId

open class CountryStatisticsRealm() : RealmObject(){

    @PrimaryKey @RealmField("_id") var id: ObjectId = ObjectId()

    var active : Int = 0
    var recovered : Int = 0
    var deaths : Int = 0
    var confirmed : Int = 0
    var country : String? = ""
    var year : String? = ""


}