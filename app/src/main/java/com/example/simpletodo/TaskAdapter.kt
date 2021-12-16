package com.example.simpletodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * A bridge that tells RV how to display given data
 */
class TaskAdapter(val list: MutableList<String>, val operationListener: OperationListener) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    interface OperationListener {
        fun taskLongClicked(position: Int)
        fun taskClicked(position: Int)
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
            tvTask.text = list.get(position)

            tvTask.setOnClickListener { operationListener.taskClicked(adapterPosition) }
            tvTask.setOnLongClickListener { operationListener.taskLongClicked(adapterPosition);true }
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