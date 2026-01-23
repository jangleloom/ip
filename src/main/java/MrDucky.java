import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class MrDucky {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        Scanner sc = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();

        // Greeting message to the user
        System.out.println(line);
        System.out.println("Hello! I'm MrDucky");
        System.out.println("What can I do for you?");
        System.out.println(line);

        // Loop: Read line, if bye then exit, else if list, print all stored items 
        // else: add to list, print added: <input>
        while (true) {
            String input = sc.nextLine();
        
            if (input.equals("bye")){
                System.out.println(line);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break; // Exit the loop and program
            }
            if (input.equals("list")) {
                System.out.println(line);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + "." + tasks.get(i));
                }
                System.out.println(line);
                continue;
            } 
            else if (input.startsWith("mark ")) {
                // Separate the index out 
                String[] parts = input.split(" ", 2);
                int index = Integer.parseInt(parts[1].trim()) - 1;
                Task doneTask = tasks.get(index);
                // Update isDone to true 
                doneTask.isDone = true; 
                System.out.println(line);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(" " + doneTask);
                System.out.println(line);
            }
            else if (input.startsWith("unmark ")) {
                String[] parts = input.split(" ", 2);
                int index = Integer.parseInt(parts[1].trim()) - 1;
                Task undoneTask = tasks.get(index);
                undoneTask.isDone = false; 
                System.out.println(line);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(undoneTask);
                System.out.println(line);
            }
            else {
                Task newTask = new Task(input);
                tasks.add(newTask);
                System.out.println(line);
                System.out.println("added: " + input);
                System.out.println(line);

            }
            
        }
    }
}
