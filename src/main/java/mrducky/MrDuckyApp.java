package mrducky;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import mrducky.exception.MrDuckyException;
import mrducky.parser.Parser;
import mrducky.storage.Storage;
import mrducky.task.Deadline;
import mrducky.task.Event;
import mrducky.task.Task;
import mrducky.task.ToDo;

/**
 * Core logic for MrDucky that converts user input into response strings.
 */
public class MrDuckyApp {
    private static final String DATE_FORMAT = "d/MM/yyyy HHmm";
    private final Storage storage;
    private final List<Task> tasks;

    /**
     * Creates a MrDuckyApp with default storage path.
     */
    public MrDuckyApp() {
        this(new Storage(Path.of("data", "mrducky.txt")));
    }

    /**
     * Creates a MrDuckyApp with the given storage.
     *
     * @param storage Storage instance to load and save tasks.
     */
    public MrDuckyApp(Storage storage) {
        this.storage = storage;
        List<Task> loadedTasks;
        try {
            loadedTasks = storage.load();
        } catch (MrDuckyException e) {
            loadedTasks = new ArrayList<>();
        }
        this.tasks = loadedTasks;
    }

    /**
     * Handles a user command and returns a response message.
     *
     * @param input User input.
     * @return Response message for display.
     */
    public String getResponse(String input) {
        String trimmed = input == null ? "" : input.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        try {
            return dispatchCommand(trimmed);
        } catch (MrDuckyException e) {
            return e.getMessage();
        }
    }

    private String dispatchCommand(String trimmed) throws MrDuckyException {
        String command = Parser.getCommandWord(trimmed).toLowerCase();
        boolean hasNoArgs = trimmed.equalsIgnoreCase(command);
        switch (command) {
        case "bye":
            if (hasNoArgs) {
                return "Bye. Hope to see you again soon!";
            }
            break;
        case "list":
            if (hasNoArgs) {
                return formatTaskList();
            }
            break;
        case "help":
            if (hasNoArgs) {
                return formatHelp();
            }
            break;
        case "mark":
            return handleMark(trimmed);
        case "unmark":
            return handleUnmark(trimmed);
        case "todo":
            return handleTodo(trimmed);
        case "deadline":
            return handleDeadline(trimmed);
        case "event":
            return handleEvent(trimmed);
        case "delete":
            return handleDelete(trimmed);
        case "find":
            return handleFind(trimmed);
        default:
            break;
        }
        return "OOPS!!! I'm sorry, but I don't know what that means :-(";
    }

    private String handleMark(String input) throws MrDuckyException {
        int index = Parser.parseIndex(input, "mark");
        if (index < 0 || index >= tasks.size()) {
            throw new MrDuckyException("OOPS!!! The task index provided is out of bounds.");
        }
        Task task = tasks.get(index);
        task.mark();
        storage.save(tasks);
        return "Nice! I've marked this task as done:\n  " + task;
    }

    private String handleUnmark(String input) throws MrDuckyException {
        int index = Parser.parseIndex(input, "unmark");
        if (index < 0 || index >= tasks.size()) {
            throw new MrDuckyException("OOPS!!! The task index provided is out of bounds.");
        }
        Task task = tasks.get(index);
        task.unmark();
        storage.save(tasks);
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    private String handleTodo(String input) throws MrDuckyException {
        String desc = Parser.parseTodo(input);
        Task newTask = new ToDo(desc);
        tasks.add(newTask);
        storage.save(tasks);
        return "Got it. I've added this task:\n  " + newTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleDeadline(String input) throws MrDuckyException {
        String[] parts = Parser.parseDeadline(input);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new MrDuckyException("OOPS!!! A deadline needs a /by time.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
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
        return "Got it. I've added this task:\n  " + newTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleEvent(String input) throws MrDuckyException {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
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
        return "Got it. I've added this task:\n  " + newTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleDelete(String input) throws MrDuckyException {
        int index = Parser.parseIndex(input, "delete");
        if (index < 0 || index >= tasks.size()) {
            throw new MrDuckyException("OOPS!!! The task index provided is out of bounds.");
        }
        Task task = tasks.remove(index);
        storage.save(tasks);
        return "Noted. I've removed this task:\n  " + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleFind(String input) throws MrDuckyException {
        String keyword = input.substring(4).trim();
        if (keyword.isEmpty()) {
            throw new MrDuckyException("OOPS!!! The keyword for find cannot be empty.");
        }
        String normalizedKeyword = keyword.toLowerCase();
        List<Task> foundTasks = tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(normalizedKeyword))
                .toList();
        if (foundTasks.isEmpty()) {
            return "No matching tasks found.";
        }
        StringBuilder builder = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < foundTasks.size(); i++) {
            builder.append(i + 1).append(".").append(foundTasks.get(i));
            if (i < foundTasks.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private String formatTaskList() {
        if (tasks.isEmpty()) {
            return "Here are the tasks in your list:\n(no tasks yet)";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            builder.append(i + 1).append(".").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                builder.append("\n");
            }
        }
        return "Here are the tasks in your list:\n" + builder;
    }

    private String formatHelp() {
        return loadHelpText();
    }

    private String loadHelpText() {
        try (InputStream in = MrDuckyApp.class.getResourceAsStream("/help.txt")) {
            if (in == null) {
                return "Help file not found.";
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return ("Could not load help file information.");
        }
    }
}

