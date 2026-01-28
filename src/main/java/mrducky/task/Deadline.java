package mrducky.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Represents a deadline task with a due time.
 */
public class Deadline extends Task {
    private LocalDateTime due;

    /**
     * Creates a deadline task with a description and due time.
     *
     * @param description Task description.
     * @param due Due time for the task.
     */
    public Deadline(String description, LocalDateTime due) {
        super(description);
        this.due = due;
    }

    public LocalDateTime getDue() {
        return due;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
        return "[D]" + super.toString() + " (by: " + due.format(formatter) + ")";
    }
}
