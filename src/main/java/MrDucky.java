import java.util.Scanner;

public class MrDucky {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        // Read user input
        Scanner sc = new Scanner(System.in);

        System.out.println(line);
        System.out.println("Hello! I'm MrDucky");
        System.out.println("What can I do for you?");
        System.out.println(line);

        while (true) {
            String input = sc.nextLine();
        
            if (input.equals("bye")){
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break; // Exit the loop and program
            }
            else {
                System.out.println("Quack! You said: " + input);
                System.out.println(line);
            } 
        }
        sc.close();
    }
}
