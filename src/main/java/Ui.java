import java.util.Scanner;
import java.util.List;

/**
 * Handles user interactions for the chatbot.
 */ 
public class Ui {
    private final Scanner scanner;
    private static final String LINE = "____________________________________________________________"; 

    /**  
     * Initializes the UI with a new Scanner for user input.
    */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message.
    */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm MrDucky");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Displays the goodbye message.
    */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays a line separator.
    */
    public void showLine() {
        System.out.println(LINE);
    }
    
    /**
     * Reads a command from the user.
     *
     * @return The user input command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }
    
    /**
     * Displays a message when a task is added.
     *
     * @param task The task that was added.
     * @param totalTasks The total number of tasks after addition.
     */
    public void showAddedTask(Task task, int totalTasks) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a message when a task is deleted.
     *
     * @param task The task that was deleted.
     * @param totalTasks The total number of tasks after deletion.
     */
    public void showDeletedTask(Task task, int totalTasks) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a message when a task is marked as done.
     *
     * @param task The task that was marked.
     */
    public void showMarkedTask(Task task) {
        showLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        showLine();
    }

    /**
     * Displays a message when a task is unmarked as not done.
     *
     * @param task The task that was unmarked.
     */
    public void showUnmarkedTask(Task task) {
        showLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        showLine();
    }

    /**
     * Displays an error message when loading data fails.
     *
     * @param message The error message.
     */
    public void showLoadingError(String message) {
        showLine();
        System.out.println("Warning! Could not read data file: " + message);
        showLine();
    }

    /**
     * Displays an error message.
     *
     * @param message The error message.
     */
    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Displays the list of tasks.
     *
     * @param tasks The list of tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        showLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        showLine();
    }
}