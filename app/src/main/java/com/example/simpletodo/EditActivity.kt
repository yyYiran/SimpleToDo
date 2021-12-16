package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.title = "Edit"

        // find (sub)Views
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val etEdit = findViewById<EditText>(R.id.etEdit)

        btnCancel.setOnClickListener { finish()}

        // Access parent data
        etEdit.setText(intent.getStringExtra("task_text"))
        val position = intent.getIntExtra("task_position", 0)

        // Return updated task to MainActivity
        btnSave.setOnClickListener {
            val intent = Intent()
            intent.putExtra("task_text", etEdit.text.toString())
            intent.putExtra("task_position", position)
            setResult(RESULT_OK, intent)
            finish()
        }

    }
}