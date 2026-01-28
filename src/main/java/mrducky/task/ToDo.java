package mrducky.task;

/**
 * Represents a to-do task.
 */
public class ToDo extends Task {
    /**
     * Creates a to-do task with the given description.
     *
     * @param description Task description.
     */
    public ToDo(String description) { 
        super(description); 
    }
    @Override public String toString() { 
        return "[T]" + super.toString(); 
    }
}
