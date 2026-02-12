package mrducky.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mrducky.exception.MrDuckyException;
import mrducky.task.Deadline;
import mrducky.task.Event;
import mrducky.task.Task;
import mrducky.task.ToDo;

/**
 * Stores and loads tasks to and from disk.
 */
public class Storage {
    private final Path filePath;
    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    /**
     * Creates a storage handler that reads and writes to the given file path.
     *
     * @param filePath Path to the data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns tasks loaded from the data file.
     *
     * @return List of tasks loaded from disk.
     */
    public List<Task> load() throws MrDuckyException {
        // If file/folder doesn't exist, create new ArrayList<>()
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }
        // Read lines
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            // Handle exception
            logger.log(Level.WARNING, "Could not read data file: " + filePath, e);
            throw new MrDuckyException("Could not read data file: " + filePath);
        }
        return tasks;
    }

    /**
     * Saves the given tasks to the data file.
     *
     * @param tasks Tasks to save.
     */
    public void save(List<Task> tasks) throws MrDuckyException {
        // Ensure parent folder exists
        try {
            Path parentDir = filePath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            // Create empty list to hold lines
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                // Convert each Task to a line
                String line = formatTask(task);
                lines.add(line);
            }
            // Write lines to file
            Files.write(filePath, lines);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not write to data file: " + filePath, e);
            throw new MrDuckyException("Could not write to data file: " + filePath);
        }
    }


    private Task parseLine(String line) {
        // Turn line into Task object (ToDo, Deadline, Event)
        // Split line by " | " (type | isDone | description | ... )
        // e.g. D | 1 | return book | June 6th
        String[] parts = line.split("\\s*\\|\\s*");

        if (parts.length < 3) {
            // Invalid line format, handle accordingly
            return null;
        }

        // Determine type based on first part
        // T - new ToDo(description)
        // D - new Deadline(description | due)
        // E - new Event(description | from | to)
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
        case "T":
            task = new ToDo(description);
            break;
        case "D":
            if (parts.length < 4) {
                return null; // Invalid Deadline format
            }
            LocalDateTime dueDate = LocalDateTime.parse(parts[3]);
            task = new Deadline(description, dueDate);
            break;
        case "E":
            if (parts.length < 5) {
                return null; // Invalid Event format
            }
            LocalDateTime from = LocalDateTime.parse(parts[3]);
            LocalDateTime to = LocalDateTime.parse(parts[4]);
            task = new Event(description, from, to);
            break;
        default:
            return null; // Unknown task type
        }
        task.setDone(isDone);
        return task;
    }

    private String formatTask(Task task) {
        String done = task.isDone() ? "1" : "0";

        if (task instanceof ToDo) {
            return "T | " + done + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return "D | " + done + " | " + task.getDescription() + " | " + d.getDueDate();
        } else if (task instanceof Event) {
            Event e = (Event) task;
            return "E | " + done + " | " + task.getDescription() + " | " + e.getFromTime() + " | " + e.getToTime();
        } else {
            throw new IllegalArgumentException("Unknown task type");
        }
    }
}
