package mrducky.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Represents a deadline task with a due time.
 */
public class Deadline extends Task {
    private LocalDateTime dueDate;

    /**
     * Creates a deadline task with a description and due time.
     *
     * @param description Task description.
     * @param dueDate Due time for the task.
     */
    public Deadline(String description, LocalDateTime dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
        return "[D]" + super.toString() + " (by: " + dueDate.format(formatter) + ")";
    }
}
