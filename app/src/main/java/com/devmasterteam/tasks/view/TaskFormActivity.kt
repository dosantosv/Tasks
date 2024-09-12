package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityRegisterBinding
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.RegisterViewModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority: List<PriorityModel> = mutableListOf()
    private var taskIdentification = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener{ handleDate() }
        binding.buttonDate.setOnClickListener{ handleSave() }

        viewModel.loadPriorities()

        loadDataFromActivity()

        observe()

        // Layout
        setContentView(binding.root)
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val duoDate = dateFormat.format(calendar.time)
        binding.buttonDate.text = duoDate
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras

        if(bundle != null) {
            taskIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.getTaskPerId(taskIdentification)
        }

    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0

        for (l in listPriority) {

            if (l.id == priorityId)
                break

            index++
        }

        return index
    }

    private fun observe() {
        viewModel.priorityList.observe(this) {
            listPriority = it
            val list = mutableListOf<String>()

            for (item in it) {
                list.add(item.description)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }

        viewModel.taskSave.observe(this) { it ->
            if(it.sucess) {
                if(taskIdentification == 0)
                    toast("Tarefa criada com sucesso.")
                else
                    toast("Tarefa atualizada com sucesso")

                finish()
            } else {
                toast(it.message)
            }

            viewModel.task.observe(this) {
                if (!it.sucess) {
                    toast(it.message)
                    finish()
                }

                val taskModel = it.responseData

                binding.editDescription.setText(taskModel.description)
                binding.checkComplete.isChecked = taskModel.complete
                binding.spinnerPriority.setSelection(getIndex(taskModel.priorityId))

                val date = SimpleDateFormat("yyyy-MM-dd").parse(taskModel.dueDate)
                binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
            }

        }
    }

    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = taskIdentification
            this.description = binding.editDescription.text.toString()
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()

            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityId = listPriority[index].id
        }

        viewModel.save(task)
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

}