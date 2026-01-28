/**
 * Represents an event task with a start and end time.
 */
public class Event extends Task {
    private String from;
    private String to;
    
    /**
     * Creates an event task with a description, start time, and end time.
     *
     * @param description Task description.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
