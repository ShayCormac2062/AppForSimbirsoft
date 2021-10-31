package com.example.simbirsoftapp.entities

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Task(
    @PrimaryKey
    var id: Long? = 0,
    var date_start: Long? = 0,
    var date_finish: Long? = 0,
    var name: String = "",
    var description: String = ""
): RealmObject() {

    override fun toString(): String {
        return "Task(id=$id, date_start=$date_start, date_finish=$date_finish, name=$name, description=$description)"
    }

}