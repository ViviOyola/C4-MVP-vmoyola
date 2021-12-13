package co.com.misiontic.vmoyola.taskreminder.mvp;

import android.content.Context;

import java.util.List;

import co.com.misiontic.vmoyola.taskreminder.view.dto.TaskItem;

public interface MainMVP {

    interface Model {

        List<TaskItem> getTasks();

        void saveTask(TaskItem task);

        void updateTask(TaskItem item);

        void deleteTask(TaskItem task);
    }

    interface Presenter {
        void loadTasks();

        void addNewTask();

        void taskItemClicked(TaskItem item);

        void updateTask(TaskItem task);

        void deleteTask(TaskItem task);

        String validateNewTask();
    }

    interface View {

        void showTaskList(List<TaskItem> items);

        String getTaskDescription();

        void addTaskToList(TaskItem task);

        void updateTask(TaskItem task);

        void showConfirmDialog(String message, TaskItem task);

        void deleteTask(TaskItem task);

        Context getContext();

        String getLoggedUser();

        List<TaskItem> getDataTask();
    }
}
