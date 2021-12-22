package com.example.simpletodo

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random



class Task (val content: String, val priority: Priority = Priority.DEFAULT, val date: Calendar = Calendar.getInstance()) : Serializable{
    val serialVersionUID: Long = 5177222050535318633L
    // TODO: string format of date
    /**
     * toString()
     * implicitly called in saveToFile() in MainActivity
     * Format content and priority to store in dataFile
     */
    override fun toString(): String {
        return "$content,${priority.name},${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH)}-${date.get(Calendar.DAY_OF_MONTH)}"
    }

    companion object {
        fun createManyTasks(): List<Task> {
            val list = mutableListOf<Task>()
            for (i in 1..10) {
                list.add(Task("Task $i", Priority.random()))
            }
            return list
        }

        /**
         * parseStringsToTasks
         * Convert a list of Strings to a list of tasks
         * called in loadFromFile() in MainActivity
         */
        fun parseStringsToTasks(listOfStrings: List<String>): MutableList<Task> {
            val listOfTasks = mutableListOf<Task>()
            if (!listOfStrings.isNullOrEmpty()) {
                for (str in listOfStrings) {
                    val date: String = str.substringAfterLast(",")
                    val priority: String = str.dropLast(date.length + 1).substringAfterLast(",")
                    val content: String = str.dropLast(priority.length + date.length + 2)

                    val dateAsList = date.split("-").map { it.toInt() }
                    val dateAsCalendar = Calendar.getInstance()
                    dateAsCalendar.set(dateAsList[0], dateAsList[1], dateAsList[2])
                    listOfTasks.add(
                        Task(
                            content,
                            when (priority) {
                                Priority.ONE.name -> Priority.ONE
                                Priority.TWO.name -> Priority.TWO
                                Priority.THREE.name -> Priority.THREE
                                else -> Priority.DEFAULT
                            },
                            dateAsCalendar
                        )
                    )
                }
            }
            return listOfTasks
        }
    }
}