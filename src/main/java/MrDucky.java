import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class MrDucky {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        Scanner sc = new Scanner(System.in);
        List<String> items = new ArrayList<>();

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
                for (int i = 0; i < items.size(); i++) {
                    System.out.println((i + 1) + "." + items.get(i));
                }
                continue;
            } 
            items.add(input);
            System.out.println(line);
            System.out.println("added: " + input);
            System.out.println(line);
        }
    }
}
