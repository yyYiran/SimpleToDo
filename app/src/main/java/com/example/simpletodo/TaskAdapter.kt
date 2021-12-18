package com.example.simpletodo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.Priority.Companion.setColorOfPriority

/**
 * A bridge that tells RV how to display given data
 */
class TaskAdapter(val list: MutableList<Task>, val operationListener: OperationListener) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    interface OperationListener {
        fun onTaskLongClicked(position: Int)
        fun onTaskClicked(position: Int)
    }

    /**
     * Provide a direct reference to each of the (sub)views within a data item
    Used to cache the views within the item layout for fast access
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize subViews of a row
        val tvTask = itemView.findViewById<TextView>(R.id.tvTask)

        // Set data and methods for each subview
        fun setDataAndMethods(position: Int) {
            val task = list.get(position)
            tvTask.text = task.content.lowercase().replaceFirstChar { it.uppercase() }
            // TODO: tvTask set background color
            tvTask.setColorOfPriority(task.priority)

            tvTask.setOnClickListener { operationListener.onTaskClicked(adapterPosition) }
            tvTask.setOnLongClickListener { operationListener.onTaskLongClicked(adapterPosition);true }
        }
    }

    /**
     * onCreateViewHolder: inflating a layout from XML and returning the holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val taskView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_list_item, parent, false)
        return ViewHolder(taskView)
    }

    // onBindViewHolder: populating data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.setDataAndMethods(position)

    override fun getItemCount(): Int = list.size
}


