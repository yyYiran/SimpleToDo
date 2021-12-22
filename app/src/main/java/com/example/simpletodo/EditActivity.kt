package com.example.simpletodo

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.simpletodo.Priority.Companion.getPriorityofColor
import com.example.simpletodo.Priority.Companion.setColorOfPriority

class EditActivity : AppCompatActivity(), PriorityDialogFragment.PriorityDialogListener{
    var newPriority: Priority = Priority.DEFAULT
    lateinit var btnPriority: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.title = "Edit Task"

        // find (sub)Views
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val etEdit = findViewById<EditText>(R.id.etEdit)
        btnPriority = findViewById(R.id.btnPrioritySelectorEdit)

        btnCancel.setOnClickListener { finish()}

        // Access parent/original data
        val originalTask: Task = intent.getSerializableExtra(KEY_TASK_BUNDLE) as Task
        etEdit.setText(originalTask.content)
        etEdit.setColorOfPriority(originalTask.priority)
        btnPriority.setColorOfPriority(originalTask.priority)
        val position = intent.getIntExtra(KEY_TASK_POS, 0)
//        etEdit.setText(intent.getStringExtra(KEY_TASK_TEXT))
//        val position = intent.getIntExtra(KEY_TASK_POS, 0)

        // Return updated task to MainActivity
        btnSave.setOnClickListener {
            val intent = Intent()
//            intent.putExtra("task_text", etEdit.text.toString())
            intent.putExtra(KEY_TASK_POS, position)
            // TODO: Input updated priority, rather than (fixed) ONE
            intent.putExtra(KEY_TASK_BUNDLE, Task(etEdit.text.toString(), newPriority))
            setResult(RESULT_OK, intent)
            finish()
        }

        btnPriority.setOnClickListener {
            PriorityDialogFragment.showPriorityDialog(this,
                getPriorityofColor(this, (it.background as ColorDrawable).color))
        }

    }

    override fun onPrioritySelected(priority: Priority) {
        newPriority = priority
        btnPriority.setColorOfPriority(priority)
    }
}