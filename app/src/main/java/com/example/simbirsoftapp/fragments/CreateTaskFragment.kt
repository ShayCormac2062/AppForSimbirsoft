package com.example.simbirsoftapp.fragments

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.applandeo.materialcalendarview.EventDay
import com.example.simbirsoftapp.R
import com.example.simbirsoftapp.databinding.FragmentCreateTaskBinding
import com.example.simbirsoftapp.entities.Task
import com.example.simbirsoftapp.services.RealmService
import com.example.simbirsoftapp.services.TimeService
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import java.util.*

class CreateTaskFragment(realm: Realm, day: EventDay?) : Fragment() {

    private val selectedTime: Calendar = Calendar.getInstance()
    private var mRealm = realm
    private lateinit var binding: FragmentCreateTaskBinding
    private var eventDay: EventDay? = day
    private var realmService = RealmService()
    private var timeService = TimeService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            createTaskBtn.setOnClickListener {
                if ((btnStartTime.text != "Указать сроки начала") && (btnEndTime.text != "Указать сроки окончания")) {
                    realmService.addTask(mRealm, binding.vtName.text.toString(),
                        binding.vtDescrption.text.toString(),
                        btnStartTime.text.toString(),
                        btnEndTime.text.toString(),
                        view)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, TasksFragment(mRealm))
                        .commit()
                } else {
                    Snackbar
                        .make(it, "Вам необходимо указать дату начала и дату окончания задачи", 2000)
                        .show()
                }
            }
            btnStartTime.setOnClickListener {
                showTimePicker(0)
            }
            btnEndTime.setOnClickListener {
                showTimePicker(1)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showTimePicker(num: Int){
        TimePickerDialog(
            context, { _, hour, minute ->
                eventDay?.calendar?.set(Calendar.HOUR_OF_DAY, hour)
                eventDay?.calendar?.set(Calendar.MINUTE, minute)
                eventDay?.calendar?.set(Calendar.SECOND, 0)
                if (num == 0) {
                    binding.btnStartTime.text = timeService.convertToTime(timeService.getDayTime(eventDay))
                } else {
                    binding.btnEndTime.text = timeService.convertToTime(timeService.getDayTime(eventDay))
                }
            },  selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
            true
        ).show()
    }


}