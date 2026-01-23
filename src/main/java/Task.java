public class Task {
    String description;
    boolean isDone;
    
    Task(String description) {
        this.isDone = false;
        this.description = description;
    }

    @Override 
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }

}
