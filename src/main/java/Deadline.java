public class Deadline extends Task {
    private String due;
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
