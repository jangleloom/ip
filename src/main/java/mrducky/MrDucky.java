package mrducky;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import mrducky.exception.DukeException;
import mrducky.parser.Parser;
import mrducky.storage.Storage;
import mrducky.task.Deadline;
import mrducky.task.Event;
import mrducky.task.Task;
import mrducky.task.ToDo;
import mrducky.ui.Ui;

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
        String trimmed = input == null ? "" : input.trim();
        if (trimmed.isEmpty()) {
            return false;
        }

        String command = Parser.getCommandWord(trimmed).toLowerCase();
        boolean hasNoArgs = trimmed.equalsIgnoreCase(command);
        if (command.equals("bye") && hasNoArgs) {
            ui.showGoodbye();
            return true;
        } else if (command.equals("list") && hasNoArgs) {
            ui.showTaskList(tasks);
            return false;
        } else if (command.equals("help") && hasNoArgs) {
            ui.showHelp();
            return false;
        } else if (command.equals("mark")) {
            int index = Parser.parseIndex(trimmed, "mark");
            Task t = tasks.get(index);
            t.mark();
            storage.save(tasks);
            ui.showMarkedTask(t);
            return false;
        } else if (command.equals("unmark")) {
            int index = Parser.parseIndex(trimmed, "unmark");
            Task t = tasks.get(index);
            t.unmark();
            storage.save(tasks);
            ui.showUnmarkedTask(t);
            return false;
        } else if (command.equals("todo")) {
            String desc = Parser.parseTodo(trimmed);
            if (desc.isEmpty()) {
                throw new DukeException("OOPS!!! The description of a todo cannot be empty.");
            }
            Task newTask = new ToDo(desc);
            tasks.add(newTask);
            storage.save(tasks);
            ui.showAddedTask(newTask, tasks.size());
            return false;
        } else if (command.equals("deadline")) {
            String[] parts = Parser.parseDeadline(trimmed);
            if (parts[0].isEmpty()) {
                throw new DukeException("OOPS!!! The description of a deadline cannot be empty.");
            }
            // Parse date string to LocalDateTime
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
        } else if (command.equals("event")) {
            String details = trimmed.substring(5).trim();
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
        } else if (command.equals("delete")) {
            int index = Parser.parseIndex(trimmed, "delete");
            Task t = tasks.remove(index);
            storage.save(tasks);
            ui.showDeletedTask(t, tasks.size());
            return false;
        } else if (command.equals("find")) {
            String keyword = trimmed.substring(4).trim();
            if (keyword.isEmpty()) {
                throw new DukeException("OOPS!!! The keyword for find cannot be empty.");
            }
            String normalizedKeyword = keyword.toLowerCase();
            List<Task> foundTasks = tasks.stream()
                    .filter(task -> task.getDescription().toLowerCase().contains(normalizedKeyword))
                    .toList();
            ui.showFoundTasks(foundTasks);
            return false;

        }
        throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

}
