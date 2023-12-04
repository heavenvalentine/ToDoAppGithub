package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ButtonBarLayout
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskActivity: DetailTaskActivity
    private lateinit var viewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : DONE Show detail task and implement delete action
        val dataTask = intent.getIntExtra(TASK_ID, 0)

        val tvDetailTitle: TextView = findViewById(R.id.detail_ed_title)
        val tvDetailDescription: TextView = findViewById(R.id.detail_ed_description)
        val tvDetailDate: TextView = findViewById(R.id.detail_ed_due_date)
        val btnDeleteTask: Button = findViewById(R.id.btn_delete_task)

        val factory = ViewModelFactory.getInstance(this)
        //hubungin activity ini dg viewmodel
        viewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        viewModel.setTaskId(dataTask)

        viewModel.task.observe(this){
            tvDetailTitle.text = it.title
            tvDetailDescription.text = it.description
            tvDetailDate.text = DateConverter.convertMillisToString(it.dueDateMillis)
        }

        btnDeleteTask.setOnClickListener{
            viewModel.deleteTask()
            finish()
        }
    }
}