/**
 * Represents a deadline task with a due time.
 */
public class Deadline extends Task {
    private String due;

    /**
     * Creates a deadline task with a description and due time.
     *
     * @param description Task description.
     * @param due Due time for the task.
     */
    public Deadline(String description, String due) {
        super(description);
        this.due = due;
    }

    public String getDue() {
        return due;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + due + ")";
    }
}
