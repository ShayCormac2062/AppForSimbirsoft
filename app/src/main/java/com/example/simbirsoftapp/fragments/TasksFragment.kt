package com.example.simbirsoftapp.fragments

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
import com.example.simbirsoftapp.services.RealmService
import com.example.simbirsoftapp.services.TimeService
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import kotlin.IllegalStateException

class TasksFragment(day: EventDay?, realm: Realm) : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private var mRealm = realm
    private var timeService = TimeService()
    private var realmService = RealmService()
    private var eventDay: EventDay? = day

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(layoutInflater)
        changeDay(eventDay)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.calendarView.setOnDayClickListener { day ->
            eventDay = day
            changeDay(day)
        }
        binding.createTaskBtn.setOnClickListener {
            if (eventDay != null) {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, CreateTaskFragment(this.mRealm, eventDay))
                    .addToBackStack(null)
                    .commit()
                binding.taskList.adapter?.notifyDataSetChanged()
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
            taskList.adapter = adapter
            adapter.apply {
                completeClickListener = {
                    view?.let { it1 ->
                        context?.let { it2 ->
                            realmService.deleteTask(it2, it, mRealm, it1,
                                taskList.adapter as TaskAdapter
                            )
                        }
                    }
                }
                clickListener = {
                    if (mRealm.where(Task::class.java).findAll().contains(it)){
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, TaskInfoFragment(it))
                            .addToBackStack(null)
                            .commit()
                    } else {
                        Snackbar.make(binding.root, "Вы уже удалили эту задачу!", 1500)
                            .show()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun changeDay(day: EventDay?) {
        val tasks = realmService.getSortedRealm(day, mRealm)
        init(TaskAdapter(tasks))
        if (tasks.isEmpty()) {
            binding.noTasksNotification.visibility = View.VISIBLE
        } else {
            binding.noTasksNotification.visibility = View.GONE
        }
        binding.currentDate.text = day?.let { timeService.getMonthFromTime(it) }
    }
}