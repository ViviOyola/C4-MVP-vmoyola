package co.com.misiontic.vmoyola.taskreminder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.com.misiontic.vmoyola.taskreminder.R;
import co.com.misiontic.vmoyola.taskreminder.mvp.MainMVP;
import co.com.misiontic.vmoyola.taskreminder.view.dto.TaskItem;

public class MainInteractor implements MainMVP.Model {

    private List<TaskItem> tempTaskItems;

    private Context context;
    private String loggedUser;

    public MainInteractor(MainMVP.View view) {
        tempTaskItems = new ArrayList<>();
        context = view.getContext();
        loggedUser = view.getLoggedUser();
        retrievePersistedTasks();
    }

    @Override
    public List<TaskItem> getTasks() {
        return tempTaskItems;
    }

    private void retrievePersistedTasks() {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.tasks_persistence), Context.MODE_PRIVATE);

        List<String> tasksToLoad = new ArrayList<>(sharedPref.getStringSet(loggedUser, Collections.<String>emptySet()));

        Collections.sort(tasksToLoad);

        for (String task : tasksToLoad){
            String[] taskFields = task.split("::");
            if(taskFields.length>2) {
                tempTaskItems.add(new TaskItem(taskFields[0], taskFields[1], taskFields[2]));
            }
        }
    }

    @Override
    public void saveTask(TaskItem task) {
        persistTask();
    }

    private void persistTask(){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.tasks_persistence), Context.MODE_PRIVATE);

        Set<String> tasksToSave = new HashSet<>();

        for (TaskItem taskItem : tempTaskItems) {
            tasksToSave.add(buildTaskIdentifier(taskItem));
        }

        sharedPref.edit().putStringSet(loggedUser, tasksToSave).commit();
    }

    @Override
    public void updateTask(TaskItem item) {
        persistTask();
    }

    @Override
    public void deleteTask(TaskItem task) {
        persistTask();
    }

    public String buildTaskIdentifier(TaskItem taskItem){
        return taskItem.getDescription() + "::" + taskItem.getDate() + "::" + taskItem.getState().toString();
    }
}
