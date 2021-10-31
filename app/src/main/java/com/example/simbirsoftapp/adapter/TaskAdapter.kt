package com.example.simbirsoftapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoftapp.databinding.BriefTaskViewBinding
import com.example.simbirsoftapp.entities.Task
import com.example.simbirsoftapp.services.TimeService

class TaskAdapter(tasks1: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val tasks = tasks1
    private var timeService = TimeService()
    var completeClickListener: ((taskModel: Task) -> Unit)? = null
    var clickListener: ((taskModel: Task) -> Unit)? = null

    inner class TaskViewHolder(
        private val binding: BriefTaskViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(item: Task) = with(binding) {
            allTasksTvName.text = item.name
            taskTime.text = "${timeService.convertToTimeHourMinute(item.date_start)}\n-\n${timeService.convertToTimeHourMinute(item.date_finish)}"
            deleteTaskBtn.setOnClickListener {
                completeClickListener?.invoke(item)
            }
            briefTaskView.setOnClickListener {
                clickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(
            BriefTaskViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        tasks[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = tasks.size
}