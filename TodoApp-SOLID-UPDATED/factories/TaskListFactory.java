package todoapp.factories;

import todoapp.models.TaskList;

public class TaskListFactory {

    // Cria uma lista pessoal
    public static TaskList createPersonalTaskList(String name, String owner) {
        return new TaskList(name, owner);
    }
}