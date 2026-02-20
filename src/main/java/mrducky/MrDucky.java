package mrducky;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;

import mrducky.exception.MrDuckyException;
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
        List<Task> tasks;
        try {
            tasks = storage.load();
        } catch (MrDuckyException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new ArrayList<>();
        }

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            try {
                // Handles the input, tasks list, and line separator for printing
                if (handleInput(input, tasks, storage, ui)) {
                    break;
                }
            } catch (MrDuckyException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Processes user input and performs corresponding actions.
     * @param input
     * @param tasks
     * @param storage
     * @param ui
     * @return
     * @throws MrDuckyException
     */
    private static boolean handleInput(String input, List<Task> tasks, Storage storage, Ui ui) throws MrDuckyException {
        String trimmed = input == null ? "" : input.trim();
        if (trimmed.isEmpty()) {
            return false;
        }

        String command = Parser.getCommandWord(trimmed).toLowerCase();
        boolean hasNoArgs = trimmed.equalsIgnoreCase(command);
        switch (command) {
        case "bye":
            if (hasNoArgs) {
                ui.showGoodbye();
                return true;
            }
            break;
        case "list":
            if (hasNoArgs) {
                ui.showTaskList(tasks);
                return false;
            }
            break;
        case "help":
            if (hasNoArgs) {
                ui.showHelp();
                return false;
            }
            break;
        case "mark":
            handleMark(trimmed, tasks, storage, ui);
            return false;
        case "unmark":
            handleUnmark(trimmed, tasks, storage, ui);
            return false;
        case "todo":
            handleTodo(trimmed, tasks, storage, ui);
            return false;
        case "deadline":
            handleDeadline(trimmed, tasks, storage, ui);
            return false;
        case "event":
            handleEvent(trimmed, tasks, storage, ui);
            return false;
        case "delete":
            handleDelete(trimmed, tasks, storage, ui);
            return false;
        case "find":
            handleFind(trimmed, tasks, ui);
            return false;
        default:
            break;
        }
        throw new MrDuckyException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    private static void handleMark(String input, List<Task> tasks, Storage storage, Ui ui) throws MrDuckyException {
        int index = Parser.parseIndex(input, "mark");
        validateIndex(index, tasks.size());
        Task task = tasks.get(index);
        task.mark();
        storage.save(tasks);
        ui.showMarkedTask(task);
    }

    private static void handleUnmark(String input, List<Task> tasks, Storage storage, Ui ui) throws MrDuckyException {
        int index = Parser.parseIndex(input, "unmark");
        validateIndex(index, tasks.size());
        Task task = tasks.get(index);
        task.unmark();
        storage.save(tasks);
        ui.showUnmarkedTask(task);
    }

    private static void handleTodo(String input, List<Task> tasks, Storage storage, Ui ui) throws MrDuckyException {
        String desc = Parser.parseTodo(input);
        if (desc.isEmpty()) {
            throw new MrDuckyException("OOPS!!! The description of a todo cannot be empty.");
        }
        Task newTask = new ToDo(desc);
        tasks.add(newTask);
        storage.save(tasks);
        ui.showAddedTask(newTask, tasks.size());
    }

    private static void handleDeadline(String input, List<Task> tasks, Storage storage, Ui ui) throws MrDuckyException {
        String[] parts = Parser.parseDeadline(input);
        if (parts[0].isEmpty()) {
            throw new MrDuckyException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new MrDuckyException("OOPS!!! A deadline needs a /by time.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");
        LocalDateTime due;
        try {
            due = LocalDateTime.parse(parts[1].trim(), formatter);
        } catch (DateTimeParseException e) {
            throw new MrDuckyException("OOPS!!! Please use d/MM/yyyy HHmm for deadlines. "
                    + "Example: 2/12/2019 1800");
        }
        Task newTask = new Deadline(parts[0].trim(), due);
        tasks.add(newTask);
        storage.save(tasks);
        ui.showAddedTask(newTask, tasks.size());
    }

    private static void handleEvent(String input, List<Task> tasks, Storage storage, Ui ui) throws MrDuckyException {
        String details = input.substring(5).trim();
        if (details.isEmpty()) {
            throw new MrDuckyException("OOPS!!! The description of an event cannot be empty.");
        }
        String[] parts = details.split(" /from ", 2);
        if (parts.length < 2) {
            throw new MrDuckyException("OOPS!!! An event needs a /from time.");
        }
        String[] timeParts = parts[1].split(" /to ", 2);
        if (timeParts.length < 2 || timeParts[0].trim().isEmpty() || timeParts[1].trim().isEmpty()) {
            throw new MrDuckyException("OOPS!!! An event needs both /from and /to times.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");
        LocalDateTime from;
        LocalDateTime to;
        try {
            from = LocalDateTime.parse(timeParts[0].trim(), formatter);
            to = LocalDateTime.parse(timeParts[1].trim(), formatter);
        } catch (DateTimeParseException e) {
            throw new MrDuckyException("OOPS!!! Please use d/MM/yyyy HHmm for events. "
                    + "Example: 2/12/2019 1800");
        }
        Task newTask = new Event(parts[0].trim(), from, to);
        tasks.add(newTask);
        storage.save(tasks);
        ui.showAddedTask(newTask, tasks.size());
    }

    private static void handleDelete(String input, List<Task> tasks, Storage storage, Ui ui) throws MrDuckyException {
        int index = Parser.parseIndex(input, "delete");
        validateIndex(index, tasks.size());
        Task task = tasks.remove(index);
        storage.save(tasks);
        ui.showDeletedTask(task, tasks.size());
    }

    private static void handleFind(String input, List<Task> tasks, Ui ui) throws MrDuckyException {
        String keyword = input.substring(4).trim();
        if (keyword.isEmpty()) {
            throw new MrDuckyException("OOPS!!! The keyword for find cannot be empty.");
        }
        String normalizedKeyword = keyword.toLowerCase();
        List<Task> foundTasks = tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(normalizedKeyword))
                .toList();
        ui.showFoundTasks(foundTasks);
    }

    private static void validateIndex(int index, int size) throws MrDuckyException {
        if (index < 0 || index >= size) {
            throw new MrDuckyException("OOPS!!! The task index provided is out of bounds.");
        }
    }
}

