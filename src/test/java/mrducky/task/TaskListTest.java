package mrducky.task;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests task list operations.
 */
public class TaskListTest {

    @Test
    public void markTask_marksTaskAsDone() {
        TaskList tasks = new TaskList(new ArrayList<>());
        Task task = new ToDo("read book");
        tasks.addTask(task);

        tasks.markTask(0);

        assertTrue(task.isDone());
    }

    @Test
    public void unmarkTask_marksTaskAsNotDone() {
        TaskList tasks = new TaskList(new ArrayList<>());
        Task task = new ToDo("read book");
        tasks.addTask(task);
        tasks.markTask(0);

        tasks.unmarkTask(0);

        assertFalse(task.isDone());
    }
}
