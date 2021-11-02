package com.example.simbirsoftapp.services

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.applandeo.materialcalendarview.EventDay
import com.example.simbirsoftapp.entities.Task
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import java.lang.IllegalStateException

class RealmService {

    private var timeService = TimeService()

    fun getSortedRealm(day: EventDay, mRealm: Realm): List<Task> {
        return mRealm
            .where(Task::class.java)
            .findAll().filter { task: Task ->
                //Понимаю, что это плохой тон, но без этого, почему-то, ничего не выходит...
                task.date_start!! >= timeService.getDayTime(day)
                        && task.date_finish!! <= timeService.getDayTime(day) + 86400000
            }.sortedWith(kotlin.Comparator<Task> { o1, o2 ->
                return@Comparator o1.date_start!!.compareTo(o2.date_finish!!)
            })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addTask(mRealm: Realm, name1: String, description1: String, startDate: String, finishDate: String, view: View) {
        mRealm.beginTransaction()
        var task: Task? = mRealm.createObject(Task::class.java, (0..10000).random())
        task?.let {
            with(it) {
                name = name1
                description = description1
                date_start = timeService.convertToLong(startDate)
                date_finish = timeService.convertToLong(finishDate)
            }
        }
        Snackbar.make(view, "Была добавлена задача \"${task?.name}\"", 1900).show()
        mRealm.commitTransaction()
    }

    fun deleteTask(context: Context, task: Task, mRealm: Realm, view: View) {
        AlertDialog.Builder(context).apply {
            setMessage("Вы точно хотите удалить эту задачу?")
            setPositiveButton("Да") { _, _ ->
                mRealm.beginTransaction()
                try {
                    task.deleteFromRealm()
                    //Не баг, а фича)
                    Snackbar.make(view, "Чтобы изменения вступили в силу, нажмите на текущую дату снова", 1500)
                        .show()
                } catch (e: IllegalStateException) {
                    Snackbar.make(view, "Вы уже удалили эту задачу!", 1500)
                        .show()
                }
                mRealm.commitTransaction()
            }
            setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }
}