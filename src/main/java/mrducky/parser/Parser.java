package mrducky.parser;

import mrducky.exception.DukeException;

/**
 * Parses user input commands for the task manager application.
 */
public class Parser {
    /**
     * Parses the command word from user input.
     *
     * @param input User input string.
     * @return The command word.
     */
    public static String getCommandWord(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty.");
        }
        String[] words = input.split(" ", 2);
        return words[0];
    }

    /**
     * Parses the index from user input for commands like mark, unmark, delete.
     *
     * @param input User input string.
     * @param command The command word (for error messages).
     * @return The zero-based index.
     * @throws DukeException If the index is missing or invalid.
     */
    public static int parseIndex(String input, String command) throws DukeException {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty.");
        }
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new DukeException("OOPS!!! The index for " + command + " cannot be empty.");
        }
        try {
            int index = Integer.parseInt(parts[1].trim());
            if (index <= 0) {
                throw new DukeException("OOPS!!! The index for " + command + " must be a positive number.");
            }
            return index - 1;
        } catch (NumberFormatException e) {
            throw new DukeException("OOPS!!! The index for " + command + " must be a valid number.");
        }
    }

    /**
     * Parses the description for a todo task from user input.
     *
     * @param input User input string.
     * @return The todo description.
     * @throws DukeException If the description is missing.
     */
    public static String parseTodo(String input) throws DukeException {
        String details = input.substring(4).trim();
        if (details.isEmpty()) {
            throw new DukeException("OOPS!!! The description of a todo cannot be empty.");
        }
        return details;
    }

    /**
     * Parses the description and due date for a deadline task from user input.
     *
     * @param input User input string.
     * @return An array with description at index 0 and due date at index 1.
     * @throws DukeException If the description or due date is missing or invalid.
     */
    public static String[] parseDeadline(String input) throws DukeException {
        String details = input.substring(8).trim();
        if (details.isEmpty()) {
            throw new DukeException("OOPS!!! The description of a deadline cannot be empty.");
        }
        String[] parts = details.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new DukeException("OOPS!!! A deadline needs both a description and a /by time.");
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    /**
     * Parses the description, from date, and to date for an event task from user input.
     *
     * @param input User input string.
     * @return An array with description at index 0, from date at index 1, and to date at index 2.
     * @throws DukeException If the description, from date, or to date is missing or invalid.
     */
    public static String[] parseEvent(String input) throws DukeException {
        String details = input.substring(5).trim();
        if (details.isEmpty()) {
            throw new DukeException("OOPS!!! The description of an event cannot be empty.");
        }
        String[] parts = details.split(" /from ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty()) {
            throw new DukeException("OOPS!!! An event needs a description and a /from time.");
        }
        String[] timeParts = parts[1].split(" /to ", 2);
        if (timeParts.length < 2 || timeParts[0].trim().isEmpty() || timeParts[1].trim().isEmpty()) {
            throw new DukeException("OOPS!!! An event needs both /from and /to times.");
        }
        return new String[]{parts[0].trim(), timeParts[0].trim(), timeParts[1].trim()};
    }
}
