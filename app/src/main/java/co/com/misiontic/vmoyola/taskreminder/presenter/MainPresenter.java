package co.com.misiontic.vmoyola.taskreminder.presenter;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import co.com.misiontic.vmoyola.taskreminder.model.MainInteractor;
import co.com.misiontic.vmoyola.taskreminder.mvp.MainMVP;
import co.com.misiontic.vmoyola.taskreminder.view.dto.TaskItem;
import co.com.misiontic.vmoyola.taskreminder.view.dto.TaskState;

public class MainPresenter implements MainMVP.Presenter {

    private final MainMVP.View view;
    private final MainMVP.Model model;

    public MainPresenter(MainMVP.View view) {
        this.view = view;
        this.model = new MainInteractor(view);
    }

    @Override
    public void loadTasks() {
        List<TaskItem> items = model.getTasks();

        view.showTaskList(items);
    }

    @Override
    public void addNewTask() {
        Log.i(MainPresenter.class.getSimpleName(), "Add new Task");
        String description = view.getTaskDescription();

        String date = SimpleDateFormat.getDateTimeInstance().format(new Date());

        TaskItem task = new TaskItem(description, date, TaskState.PENDING);
        view.addTaskToList(task);

        model.saveTask(task);
    }

    @Override
    public void taskItemClicked(TaskItem task) {
        String message = task.getState() == TaskState.PENDING
                ? "¿Terminada?"
                : "¿Pendiente?";
        view.showConfirmDialog(message, task);
    }

    @Override
    public void updateTask(TaskItem task) {
        task.setState(task.getState() == TaskState.PENDING ? TaskState.DONE : TaskState.PENDING);
        view.updateTask(task);
        model.updateTask(task);
    }


    @Override
    public void deleteTask(TaskItem task) {

        view.deleteTask(task);
        model.deleteTask(task);
    }

    @Override
    public String validateNewTask() {

        String description = view.getTaskDescription();

        String errorMessage = "";

        if(description.equals("")) {
            errorMessage = "La tarea debe tener una descripción no vacia!";
        } else {
            for (TaskItem taskItem : view.getDataTask()) {
                if (description.equals(taskItem.getDescription())) {
                    errorMessage = "Tarea existente, cambie el nombre de la nueva la tarea o elimine la existente";
                    break;
                }
            }
        }

       return errorMessage;
    }
}
