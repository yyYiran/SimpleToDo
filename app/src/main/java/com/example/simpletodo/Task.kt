package com.example.simpletodo

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import java.io.Serializable
import kotlin.random.Random

// TODO: more about enum class: values() as Array<Priority>
enum class Priority: Serializable{
    DEFAULT, ONE, TWO, THREE;

    companion object {
        fun random() = values()[Random.nextInt(values().size)]

        fun View.setColorOfPriority (priority: Priority) {
            this.setBackgroundColor(when (priority) {
                DEFAULT -> ContextCompat.getColor(context, R.color.white_4)
                ONE -> ContextCompat.getColor(context, R.color.red_1)
                TWO -> ContextCompat.getColor(context, R.color.yellow_2)
                THREE -> ContextCompat.getColor(context, R.color.blue_3)
            })
        }

        fun getPriorityofColor (context: Context, color: Int): Priority =
             when (color){
                    ContextCompat.getColor(context, R.color.red_1) -> ONE
                    ContextCompat.getColor(context, R.color.yellow_2) -> TWO
                    ContextCompat.getColor(context, R.color.blue_3) -> THREE
                    else -> DEFAULT
                }



        fun findRadioButtonId(priority: Priority) =
            when (priority){
                ONE -> R.id.rbP1
                TWO -> R.id.rbP2
                THREE -> R.id.rbP3
                else -> R.id.rbP4
            }
    }


}

class Task(val content: String, val priority: Priority = Priority.DEFAULT) : Serializable{
    val serialVersionUID: Long = 5177222050535318633L
    /**
     * toString()
     * Format content and priority to store in dataFile
     */
    override fun toString(): String {
        return "$content,${priority.name}"
    }

    companion object {
        fun createManyTasks(): List<Task> {
            val list = mutableListOf<Task>()
            for (i in 1..10) {
                list.add(Task("Task $i", Priority.random()))
            }
            return list
        }

        fun stringsToTasks(listOfStrings: List<String>): MutableList<Task> {
            val listOfTasks = mutableListOf<Task>()
            if (!listOfStrings.isNullOrEmpty()) {
                for (str in listOfStrings) {
                    val content = str.substringBeforeLast(",")
                    val priority: String = str.substringAfterLast(",")
//                    println(content + "|" + priority)
                    listOfTasks.add(
                        Task(
                            content, when (priority) {
                                Priority.ONE.name -> Priority.ONE
                                Priority.TWO.name -> Priority.TWO
                                Priority.THREE.name -> Priority.THREE
                                else -> Priority.DEFAULT
                            }
                        )
                    )
                }
            }
            return listOfTasks
        }
    }
}