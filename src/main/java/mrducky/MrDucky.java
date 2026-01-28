package mrducky;

import mrducky.exception.DukeException;
import mrducky.storage.Storage;
import mrducky.task.Deadline;
import mrducky.task.Event;
import mrducky.task.Task;
import mrducky.task.ToDo;
import mrducky.ui.Ui;
import java.util.List;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Runs the MrDucky chatbot application.
 */
public class MrDucky {
    /**
     * Runs the chatbot program loop.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();

        Storage storage = new Storage(Path.of("data", "mrducky.txt"));
        List<Task> tasks = storage.load();

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            try {
                // Handles the input, tasks list, and line separator for printing
                if (handleInput(input, tasks, storage, ui)) {
                    break;
                }
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    // Helper function to handle user input and perform corresponding actions on tasks list
    // Separated from main for clarity, use try/catch around each call to handle exceptions 
    private static boolean handleInput(String input, List<Task> tasks, Storage storage, Ui ui) throws DukeException {
        if (input.equals("bye")) {
            ui.showGoodbye();
            return true;
        } else if (input.equals("list")) {
            ui.showTaskList(tasks);
            return false;
        } else if (input.startsWith("mark")) {
            int index = parseIndex(input, "mark");
            Task t = tasks.get(index);
            t.mark();
            storage.save(tasks);
            ui.showMarkedTask(t);
            return false;
        } else if (input.startsWith("unmark")) {
            int index = parseIndex(input, "unmark");
            Task t = tasks.get(index);
            t.unmark();
            storage.save(tasks);
            ui.showUnmarkedTask(t);
            return false;
        } else if (input.startsWith("todo")) {
            String desc = input.substring(4).trim();
            if (desc.isEmpty()) {
                throw new DukeException("OOPS!!! The description of a todo cannot be empty.");
            }
            Task newTask = new ToDo(desc);
            tasks.add(newTask);
            storage.save(tasks);
            ui.showAddedTask(newTask, tasks.size());
            return false;
        } else if (input.startsWith("deadline")) {
            String details = input.substring(8).trim();
            if (details.isEmpty()) {
                throw new DukeException("OOPS!!! The description of a deadline cannot be empty.");
            }
            // Parse date string to LocalDateTime
            String[] parts = details.split(" /by ", 2);
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new DukeException("OOPS!!! A deadline needs a /by time.");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");
            LocalDateTime due;
            // Parse and handle invalid format
            try {
                due = LocalDateTime.parse(parts[1].trim(), formatter);
            } catch (DateTimeParseException e) {
                throw new DukeException("OOPS!!! Please use d/MM/yyyy HHmm for deadlines. "
                        + "Example: 2/12/2019 1800");
            }
            Task newTask = new Deadline(parts[0].trim(), due);
            tasks.add(newTask);
            storage.save(tasks);
            ui.showAddedTask(newTask, tasks.size());
            return false;
        } else if (input.startsWith("event")) {
            String details = input.substring(5).trim();
            if (details.isEmpty()) {
                throw new DukeException("OOPS!!! The description of an event cannot be empty.");
            }
            String[] parts = details.split(" /from ", 2);
            if (parts.length < 2) {
                throw new DukeException("OOPS!!! An event needs a /from time.");
            }
            String[] timeParts = parts[1].split(" /to ", 2);
            if (timeParts.length < 2 || timeParts[0].trim().isEmpty() || timeParts[1].trim().isEmpty()) {
                throw new DukeException("OOPS!!! An event needs both /from and /to times.");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");
            LocalDateTime from;
            LocalDateTime to;
            try {
                from = LocalDateTime.parse(timeParts[0].trim(), formatter);
                to = LocalDateTime.parse(timeParts[1].trim(), formatter);
            } catch (DateTimeParseException e) {
                throw new DukeException("OOPS!!! Please use d/MM/yyyy HHmm for events. "
                        + "Example: 2/12/2019 1800");
            }
            Task newTask = new Event(parts[0].trim(), from, to);
            tasks.add(newTask);
            storage.save(tasks);
            ui.showAddedTask(newTask, tasks.size());
            return false;
        } else if (input.startsWith("delete")) {
            int index = parseIndex(input, "delete");
            Task t = tasks.remove(index);
            storage.save(tasks);
            ui.showDeletedTask(t, tasks.size());
            return false;
        }
        throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    // Helper function to parse index from commands like mark, unmark, delete
    // Try and catch to handle invalid formats and out-of-bounds errors e.g. negative numbers or non-integers
    private static int parseIndex(String input, String command) throws DukeException {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new DukeException("OOPS!!! The task number for " + command + " cannot be empty.");
        }
        try {
            int index = Integer.parseInt(parts[1].trim()) - 1;
            if (index < 0) {
                throw new DukeException("OOPS!!! The task number for " + command + " must be positive.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new DukeException("OOPS!!! The task number for " + command + " must be a number.");
        }
    }

}
