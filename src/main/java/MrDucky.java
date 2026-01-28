import java.util.Scanner;
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
        String line = "____________________________________________________________";
        Scanner sc = new Scanner(System.in);

        Storage storage = new Storage(Path.of("data", "mrducky.txt"));
        List<Task> tasks = storage.load();

        System.out.println(line);
        System.out.println("Hello! I'm MrDucky");
        System.out.println("What can I do for you?");
        System.out.println(line);

        while (true) {
            String input = sc.nextLine();
            try {
                // Handles the input, tasks list, and line separator for printing
                if (handleInput(input, tasks, storage, line)) {
                    break;
                }
            } catch (DukeException e) {
                System.out.println(line);
                System.out.println(e.getMessage());
                System.out.println(line);
            }
        }
    }

    // Helper function to handle user input and perform corresponding actions on tasks list
    // Separated from main for clarity, use try/catch around each call to handle exceptions 
    private static boolean handleInput(String input, List<Task> tasks, Storage storage, String line) throws DukeException {
        if (input.equals("bye")) {
            System.out.println(line);
            System.out.println("Bye. Hope to see you again soon!");
            System.out.println(line);
            return true;
        } else if (input.equals("list")) {
            System.out.println(line);
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
            System.out.println(line);
            return false;
        } else if (input.startsWith("mark")) {
            int index = parseIndex(input, "mark");
            Task t = tasks.get(index);
            t.isDone = true;
            storage.save(tasks);
            System.out.println(line);
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + t);
            System.out.println(line);
            return false;
        } else if (input.startsWith("unmark")) {
            int index = parseIndex(input, "unmark");
            Task t = tasks.get(index);
            t.isDone = false;
            storage.save(tasks);
            System.out.println(line);
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + t);
            System.out.println(line);
            return false;
        } else if (input.startsWith("todo")) {
            String desc = input.substring(4).trim();
            if (desc.isEmpty()) {
                throw new DukeException("OOPS!!! The description of a todo cannot be empty.");
            }
            tasks.add(new ToDo(desc));
            storage.save(tasks);
            printAddMessage(tasks, line);
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
            tasks.add(new Deadline(parts[0].trim(), due));
            storage.save(tasks);
            printAddMessage(tasks, line);
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
            tasks.add(new Event(parts[0].trim(), from, to));
            storage.save(tasks);
            printAddMessage(tasks, line);
            return false;
        } else if (input.startsWith("delete")) {
            int index = parseIndex(input, "delete");
            Task t = tasks.remove(index);
            storage.save(tasks);
            System.out.println(line);
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + t);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println(line);
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

    private static void printAddMessage(List<Task> tasks, String line) {
        System.out.println(line);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks.get(tasks.size() - 1));
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(line);
    }
}
