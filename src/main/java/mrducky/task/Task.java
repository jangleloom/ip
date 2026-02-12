package mrducky.task;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    Task(String description) {
        assert description != null : "Description cannot be null";
        this.isDone = false;
        this.description = description;
    }

    /**
     * Returns the description of the task.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the task is marked as done.
     *
     * @return True if done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks the task as done.
     */
    public void mark() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void unmark() {
        isDone = false;
    }

    /**
     * Sets the done status of the task.
     *
     * @param done True if done, false otherwise.
     */
    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }

}
