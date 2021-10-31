package com.example.simbirsoftapp.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.EventDay
import com.example.simbirsoftapp.R
import com.example.simbirsoftapp.adapter.TaskAdapter
import com.example.simbirsoftapp.databinding.FragmentTasksBinding
import com.example.simbirsoftapp.entities.Task
import com.example.simbirsoftapp.services.TimeService
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import java.lang.IllegalStateException

class TasksFragment(realm: Realm) : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private var mRealm = realm
    private var timeService = TimeService()
    private var eventDay: EventDay? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        eventDay = null
        binding.calendarView.setOnDayClickListener { day ->
            eventDay = day
            val tasks = getSortedRealm(day)
            init(TaskAdapter(tasks))
            if (tasks.isEmpty()) {
                binding.noTasksNotification.visibility = View.VISIBLE
            } else {
                binding.noTasksNotification.visibility = View.GONE
            }
            binding.currentDate.text = timeService.getMonthFromTime(day)
        }
        binding.createTaskBtn.setOnClickListener {
            if (eventDay != null) {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, CreateTaskFragment(this.mRealm, eventDay))
                    .addToBackStack(null)
                    .commit()
            } else {
                Snackbar.make(it, "Выберите сначала день, в который хотите записать задачу", 1500)
                    .show()
            }
        }

    }


    private fun init(adapter: TaskAdapter) {
        binding.apply {
            taskList.layoutManager = LinearLayoutManager(requireActivity()).apply {
                orientation = RecyclerView.VERTICAL
            }
            taskList.apply {
                this.adapter = adapter
                adapter.apply {
                    completeClickListener = {
                        AlertDialog.Builder(context).apply {
                            setMessage("Вы точно хотите удалить эту задачу?")
                            setPositiveButton("Да") { _, _ ->
                                mRealm.beginTransaction()
                                try {
                                    it.deleteFromRealm()
                                    //Не баг, а фича)
                                    Snackbar.make(binding.root, "Чтобы изменения вступили в силу, нажмите на текущую дату снова", 1500)
                                        .show()
                                } catch (e: IllegalStateException) {
                                    Snackbar.make(binding.root, "Вы уже удалили эту задачу!", 1500)
                                        .show()
                                }
                                mRealm.commitTransaction()
                            }
                            setNegativeButton("Нет") { dialog, _ ->
                                dialog.dismiss()
                            }
                        }.show()
                    }
                    clickListener = {
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, TaskInfoFragment(it))
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
        }
    }

    private fun getSortedRealm(day: EventDay): List<Task> {
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
}