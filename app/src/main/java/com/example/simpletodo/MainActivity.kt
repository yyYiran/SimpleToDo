package com.example.simpletodo

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.Priority.Companion.getPriorityofColor
import com.example.simpletodo.Priority.Companion.setColorOfPriority
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

//val KEY_TASK_TEXT = "task_text"
val KEY_TASK_BUNDLE = "task_bundle"
val KEY_TASK_POS = "task_position"
val REQUEST_CODE_EDIT = 2182

class MainActivity : AppCompatActivity(), PriorityDialogFragment.PriorityDialogListener {


    val listOfTasks = ArrayList<Task>()
    lateinit var dataFile: File

    lateinit var taskAdapter: TaskAdapter

    var newPriority: Priority = Priority.DEFAULT
    lateinit var btnPriority: ImageView

    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(REQUEST_CODE_EDIT, result)
        }

    // How MainActivity deal with data from EditActivity
    private fun handleActivityResult(requestCode: Int, result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            // getData() returns intent
//            val bundle = result.data?.extras
            val intent = result.data

            when (requestCode) {
                REQUEST_CODE_EDIT -> {
                    // Get data from EditActivity
                    if (intent != null) {
                        val position: Int = intent.getIntExtra(KEY_TASK_POS, 0)
                        val taskNew = intent.getSerializableExtra(KEY_TASK_BUNDLE) as Task

                        // Update RV
                        listOfTasks.set(position, taskNew)
                        taskAdapter.notifyItemChanged(position)
                        saveToFile()
                    }
                }
                else -> Log.e("handleActivityResult", "Wrong request code")
            }
        }

    }

    // Create an anonymous object that implements interface TaskAdapter.OperationListener
    private val operationListener = object : TaskAdapter.OperationListener {
        override fun onTaskLongClicked(position: Int) {
            listOfTasks.removeAt(position)
            taskAdapter.notifyItemRemoved(position)
            saveToFile()
        }

        // MainActivity send data to and open EditActivity
        override fun onTaskClicked(position: Int) {
            // Go to EditActivity, send original task and its position
            val intent = Intent(this@MainActivity, EditActivity::class.java)
            intent.putExtra(KEY_TASK_BUNDLE, listOfTasks.get(position))
//            intent.putExtra(KEY_TASK_TEXT, listOfTasks.get(position).content)
            intent.putExtra(KEY_TASK_POS, position)

//            // handle updated task from EditActivity
            resultLauncher.launch(intent)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Data model
        dataFile = getFile()
        dataFile.createNewFile()
        loadFromFile()

        // find the btn, rv
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val etAdd = findViewById<EditText>(R.id.etAdd)
        val rvTasks = findViewById<RecyclerView>(R.id.rvTasks)
        btnPriority = findViewById(R.id.btnPrioritySelectorMain)

        /**
         * Add a task
         */
        // TODO: press `enter` to add task
        btnAdd.setOnClickListener {
            val taskToAdd: String = etAdd.text.toString()
            if (!taskToAdd.isNullOrBlank()) {
                // Update data model
                val task = Task(taskToAdd, newPriority)
                Log.d("MainA btnAdd", task.content + ", " + task.priority)
                listOfTasks.add(task)

                // Notify adapter, scroll to new item
                taskAdapter.notifyItemInserted(listOfTasks.size - 1)
                rvTasks.scrollToPosition(listOfTasks.size - 1)

                // Clear entry
                etAdd.text.clear() // or setText("")
                saveToFile()
            }
        }

        btnPriority.setOnClickListener {
            PriorityDialogFragment.showPriorityDialog(this,
                getPriorityofColor(this, (it.background as ColorDrawable).color))
        }

        // Set adapter and layoutManager for RV
        taskAdapter = TaskAdapter(listOfTasks, operationListener)
        rvTasks.adapter = taskAdapter
        rvTasks.layoutManager = LinearLayoutManager(this)

    }

    //    fun getFile():File = File(getExternalFilesDir(null), "data.txt")
    fun getFile(): File = File(applicationContext.filesDir, "data.txt")

    // model -> file
    private fun saveToFile() {
        try {
            FileUtils.writeLines(dataFile, listOfTasks)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d("MainA saveToFile()", "datafile: " + dataFile.readLines())
        Log.d("MainA saveToFile()", "listOfTasks = " + listOfTasks)
    }

    // load from file to model, when start
    private fun loadFromFile() {
        try {
            // Parse every line from dataFile, create Task, add to listOfTasks
            listOfTasks.addAll(
                Task.stringsToTasks(
                    FileUtils.readLines(
                        dataFile,
                        Charset.defaultCharset()
                    )
                )
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Log.d("MainA loadFromFile()", "datafile: " + dataFile.readLines())
        Log.d("MainA loadFromFile()", "listOfTasks = " + listOfTasks)
    }

//    fun showPriorityDialog() {
//        val priorityDialog = PriorityDialogFragment.newInstance()
//        priorityDialog.show(supportFragmentManager, "Priority picker")
//    }

    override fun onPrioritySelected(priority: Priority) {
        btnPriority.setColorOfPriority(priority)
        newPriority = priority
    }

}