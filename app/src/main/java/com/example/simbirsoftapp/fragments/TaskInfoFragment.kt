package com.example.simbirsoftapp.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.simbirsoftapp.databinding.FragmentTaskInfoBinding
import com.example.simbirsoftapp.entities.Task
import com.example.simbirsoftapp.services.TimeService

class TaskInfoFragment(task1: Task) : Fragment() {

    private lateinit var binding: FragmentTaskInfoBinding
    private var task = task1
    private var timeService = TimeService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            taskName.text = task.name
            taskDescription.text = task.description
            taskStartEnd.text = "С ${timeService.convertToTimeHourMinute(task.date_start)} до ${timeService.convertToTimeHourMinute(task.date_finish)}"
        }
    }

}