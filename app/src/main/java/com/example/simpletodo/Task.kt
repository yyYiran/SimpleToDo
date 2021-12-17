package com.example.simpletodo

import kotlin.random.Random

// TODO: more about enum class: values() as Array<Priority>
enum class Priority {
    DEFAULT, ONE, TWO, THREE;

    companion object {
        fun random() = values()[Random.nextInt(values().size)]
    }
}

class Task(val content: String, val priority: Priority = Priority.DEFAULT) {
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