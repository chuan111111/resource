package lab3;

/**
 * Hello world!
 *
 */
public class Task implements Comparable<Task>
{
    private String description;
   private int priority;

    public Task(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }

    @Override
    public int compareTo(Task other) {
        if (this.priority != other.priority) {
            return Integer.compare(other.priority, this.priority); // Note: reverse order for priority
        }
        // If priorities are the same, compare by description lexicographically
        return this.description.compareTo(other.description);
    }
    @Override
    public String toString() {
        return "Task{description='" + description + "', priority=" + priority + '}';
    }
}
