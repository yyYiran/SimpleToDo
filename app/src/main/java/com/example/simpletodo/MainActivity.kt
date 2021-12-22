package com.example.simpletodo

import android.annotation.TargetApi
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.Priority.Companion.getPriorityofColor
import com.example.simpletodo.Priority.Companion.setColorOfPriority
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

//val KEY_TASK_TEXT = "task_text"
const val KEY_TASK_BUNDLE = "task_bundle"
const val KEY_TASK_POS = "task_position"
const val REQUEST_CODE_EDIT = 2182
private const val TAG = "MainA:"

class MainActivity : AppCompatActivity(), PriorityDialogFragment.PriorityDialogListener{

    // Data model
    val listOfTasks = ArrayList<Task>()
    // Data storage
    lateinit var dataFile: File
    // ItemAdapter, access to item views like tvContent, tvDate
    lateinit var taskAdapter: TaskAdapter

    var newPriority: Priority = Priority.DEFAULT

    lateinit var btnPriority: ImageView

    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(REQUEST_CODE_EDIT, result)
        }

    // How MainActivity deal with data from EditActivity
    @TargetApi(Build.VERSION_CODES.N)
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
                else -> Log.e(TAG + "handle..Result", "Wrong request code")
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

        // Edit task content
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

        override fun onDateClicked(position: Int) {
            Log.d(TAG + "1 onDateClicked", listOfTasks.get(position).content + " clicked")
            // TODO: Select a new date for the task (DatePickerDialog)
            setDate(position)
            Log.d(TAG+"3 onDateClicked", listOfTasks.get(position).date.get(Calendar.MONTH).toString()
                    + "/" + listOfTasks.get(position).date.get(Calendar.DAY_OF_MONTH).toString())
            // TODO: save date to file
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
                    // TODO: date property of task
                val task = Task(taskToAdd, newPriority)
                Log.d(TAG + "btnAdd", task.content + ", " + task.priority)
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
        Log.d(TAG + "saveToFile", "datafile: " + dataFile.readLines())
        Log.d(TAG + "saveToFile", "listOfTasks = " + listOfTasks)
    }

    // load from file to model, when start
    private fun loadFromFile() {
        try {
            // Parse every line from dataFile, create Task, add to listOfTasks
            listOfTasks.addAll(
                Task.parseStringsToTasks(FileUtils.readLines(dataFile, Charset.defaultCharset()))
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Log.d(TAG + "loadFromFile", "datafile: " + dataFile.readLines())
        Log.d(TAG + "loadFromFile", "listOfTasks = " + listOfTasks)
    }

//    fun showPriorityDialog() {
//        val priorityDialog = PriorityDialogFragment.newInstance()
//        priorityDialog.show(supportFragmentManager, "Priority picker")
//    }

    override fun onPrioritySelected(priority: Priority) {
        btnPriority.setColorOfPriority(priority)
        newPriority = priority
    }


    fun setDate(position: Int){
        val tmpdate = listOfTasks[position].date
        val year = tmpdate.get(Calendar.YEAR)
        val month = tmpdate.get(Calendar.MONTH)
        val day = tmpdate.get(Calendar.DAY_OF_MONTH)
        Log.d(TAG + "1.5 setDate", "default date is " + month + "/" +day)
        // onDateSet is the only method of interface DatePickerDialog.OnDateSetListener
        // It's called when user change date + click "OK"
        DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener{ _, year, month, day
                -> listOfTasks[position].date.set(year, month, day)
                val datetmp = listOfTasks[position].date
                Log.d(TAG + "4 setDate", "we set day to be " + datetmp.get(Calendar.MONTH) + "/" + datetmp.get(Calendar.DAY_OF_MONTH))
                taskAdapter.notifyItemChanged(position)
                saveToFile()
                Log.d(TAG + "5 setDate", "notify adapter " + datetmp.get(Calendar.MONTH) + "/" + datetmp.get(Calendar.DAY_OF_MONTH))}, // notify adapter IMMEDIATELY
            year, month, day)
            .show()
        // When picker pops out, before you change date, display you last selected date
        val date = listOfTasks[position].date
        Log.d(TAG + "2 setDate", date.get(Calendar.MONTH).toString()+"/"+date.get(Calendar.DAY_OF_MONTH).toString())
    }


}