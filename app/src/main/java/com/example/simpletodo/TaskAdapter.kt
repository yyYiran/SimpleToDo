package com.example.simpletodo

import android.util.Log
import java.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.Priority.Companion.setColorOfPriority

/**
 * A bridge that tells RV how to display given data
 */
private const val TAG = "TaskAdapter:"

class TaskAdapter(val list: MutableList<Task>, val operationListener: OperationListener) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    interface OperationListener {
        fun onTaskLongClicked(position: Int)
        fun onTaskClicked(position: Int)
        fun onDateClicked(position: Int)
    }

    /**
     * Provide a direct reference to each of the (sub)views within a data item
    Used to cache the views within the item layout for fast access
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize subViews of a row
        val layoutTask = itemView.findViewById<ConstraintLayout>(R.id.layoutTask)
        val tvTask = itemView.findViewById<TextView>(R.id.tvTask)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)

        // Set data and methods for each subview
        //
//        @TargetApi(Build.VERSION_CODES.N)
        fun setDataAndMethods(position: Int) {
            val task = list.get(position)
            tvTask.text = task.content.lowercase().replaceFirstChar { it.uppercase() }
            // tvTask set background color
            tvTask.setColorOfPriority(task.priority)

            // TODO: set up current date by default
            tvDate.text = "${task.date.get(Calendar.MONTH)+1}/${task.date.get(Calendar.DAY_OF_MONTH)}/${task.date.get(Calendar.YEAR)}"


            tvTask.setOnClickListener { operationListener.onTaskClicked(adapterPosition) }
            layoutTask.setOnLongClickListener { operationListener.onTaskLongClicked(adapterPosition);true }
            tvDate.setOnClickListener { operationListener.onDateClicked(adapterPosition) }
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


