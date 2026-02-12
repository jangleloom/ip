package mrducky.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a start and end time.
 */
public class Event extends Task {
    private LocalDateTime fromTime;
    private LocalDateTime toTime;

    /**
     * Creates an event task with a description, start time, and end time.
     *
     * @param description Task description.
     * @param fromTime Start time of the event.
     * @param toTime End time of the event.
     */
    public Event(String description, LocalDateTime fromTime, LocalDateTime toTime) {
        super(description);
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
        return "[E]" + super.toString() + " (from: " + fromTime.format(formatter) + " to: " + toTime.format(formatter) + ")";
    }
}
