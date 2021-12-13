package co.com.misiontic.vmoyola.taskreminder.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import co.com.misiontic.vmoyola.taskreminder.LoginActivity;
import co.com.misiontic.vmoyola.taskreminder.R;
import co.com.misiontic.vmoyola.taskreminder.mvp.MainMVP;
import co.com.misiontic.vmoyola.taskreminder.presenter.MainPresenter;
import co.com.misiontic.vmoyola.taskreminder.view.adapter.TaskAdapter;
import co.com.misiontic.vmoyola.taskreminder.view.dto.TaskItem;

public class MainActivity extends AppCompatActivity implements MainMVP.View {

    private TextInputLayout tilNewTask;
    private TextInputEditText etNewTask;
    private RecyclerView rvTasks;

    private TaskAdapter taskAdapter;

    private MainMVP.Presenter presenter;

    private String loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedUser = extras.getString("loggedUser");
        } else {
            loggedUser = "";
        }

        presenter = new MainPresenter(this);

        initUI();
        presenter.loadTasks();
    }

    private void initUI() {
        tilNewTask = findViewById(R.id.til_new_task);
        tilNewTask.setEndIconOnClickListener(v -> presenter.addNewTask());
        tilNewTask.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = presenter.validateNewTask();
                if(error.equals("")) {
                    presenter.addNewTask();
                    etNewTask.setText("");
                    etNewTask.clearFocus();
                } else {
                    etNewTask.setError(error);
                    etNewTask.requestFocus();
                }
            }
        });

        etNewTask = findViewById(R.id.et_new_task);

        taskAdapter = new TaskAdapter();
        taskAdapter.setClickListener(item -> presenter.taskItemClicked(item));

        rvTasks = findViewById(R.id.rv_tasks);
        rvTasks.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rvTasks.setAdapter(taskAdapter);
    }

    @Override
    public void showTaskList(List<TaskItem> items) {
        taskAdapter.setData(items);
    }

    @Override
    public String getTaskDescription() {
        return etNewTask.getText().toString();
    }

    @Override
    public void addTaskToList(TaskItem task) {
        taskAdapter.addItem(task);
    }

    @Override
    public void updateTask(TaskItem task) {
        taskAdapter.updateTask(task);
    }

    int selectedOption = 0;
    @Override
    public void showConfirmDialog(String message, TaskItem task){

        CharSequence[] items = new CharSequence[]{message,"Eliminar"};
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_album)
                .setTitle(task.getDescription())
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedOption = which;
                    }
                })
                .setPositiveButton("Si", (dialog, which) -> {
                    if(selectedOption==0) {
                        presenter.updateTask(task);
                    } else if(selectedOption==1){
                        presenter.deleteTask(task);
                    }
                    selectedOption = 0;
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void deleteTask(TaskItem task) {
        taskAdapter.removeTask(task);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getLoggedUser() {
        return loggedUser;
    }

    @Override
    public List<TaskItem> getDataTask() {
        return taskAdapter.getDataTask();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_album)
                .setTitle("cerrar sesión")
                .setMessage("¿Desea cerrar sesión?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}