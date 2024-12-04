package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TaskScheduler {
    private PriorityQueue<Task> priorityQueue;

    public TaskScheduler() {
        priorityQueue=new PriorityQueue<>();
    }

    public void addTask(String description, int priority){
        priorityQueue.add(new Task(description,priority));
    }

    public List<Task> getTopKTasks(int k){
        List<Task> topKTasks = new ArrayList<>();
        for (int i = 0; i < k && !priorityQueue.isEmpty(); i++) {
            topKTasks.add(priorityQueue.peek());
            priorityQueue.poll();
        }
        // 将移除的任务重新放回队列
        priorityQueue.addAll(topKTasks);
        return topKTasks;
    }

    public void finishNextTask(){
        if (!priorityQueue.isEmpty()) {
            priorityQueue.poll();
        }
    }
}
