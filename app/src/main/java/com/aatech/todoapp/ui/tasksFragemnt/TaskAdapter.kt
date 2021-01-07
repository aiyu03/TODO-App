package com.aatech.todoapp.ui.tasksFragemnt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aatech.todoapp.data.Task
import com.aatech.todoapp.databinding.ItemTaskBinding

class TaskAdapter(val listener: OnItemClickListener) :
    ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallBack()) {


    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.apply {
                    root.setOnClickListener {
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            val task = getItem(position)
                            listener.onItemClick(task, binding.root)
                        }
                    }
                    checkBoxCompleted.setOnClickListener {
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            val task = getItem(position)
                            listener.onCheckBoxClick(
                                task,
                                checkBoxCompleted.isChecked
                            )
                        }
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                checkBoxCompleted.text = task.name
                textViewDescription.text = task.description
                checkBoxCompleted.isChecked = task.completed
                checkBoxCompleted.paint.isStrikeThruText = task.completed
                textViewDescription.paint.isStrikeThruText = task.completed
                labelPriority.isVisible = task.importance
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task, view: View)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    class DiffCallBack : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id
    }
}