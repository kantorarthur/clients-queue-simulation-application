package Model;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private volatile boolean running;
    private int averageServerWaitingTime;
    private int processedTasks;

    public Server()
    {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.running = true;
        this.averageServerWaitingTime = 0;
        this.processedTasks = 0;
    }

    public void stop()
    {
        this.running = false;
    }

    public BlockingQueue<Task> getTasks()
    {
        return this.tasks;
    }

    public void addTask(Task task)
    {
        try
        {
            tasks.put(task);
            this.waitingPeriod.addAndGet(task.getServiceTime());
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    public AtomicInteger getWaitingPeriod()
    {
        return this.waitingPeriod;
    }

    public void setWaitingPeriod(int waitingPeriod)
    {
        this.waitingPeriod.set(waitingPeriod);
    }

    @Override
    public void run()
    {
        while (running || !tasks.isEmpty())
        {
            try
            {
                Task task = this.tasks.peek();
                if(task != null)
                {
                    int serviceTime = task.getServiceTime();
                    while (task.getServiceTime() > 0)
                    {
                        Thread.sleep(1000);
                        task.setServiceTime(task.getServiceTime() - 1);
                        this.waitingPeriod.decrementAndGet();
                    }
                    if(task.getServiceTime() == 0)
                    {
                        this.tasks.take();
                        this.averageServerWaitingTime = this.averageServerWaitingTime + serviceTime;
                        processedTasks++;
                    }
                }
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public float getAverageServerWaitingTime()
    {
        if(processedTasks == 0)
            return 0;

        return (float) this.averageServerWaitingTime/this.processedTasks;
    }

    @Override
    public String toString()
    {
        return "Waiting period for this server: " + this.getWaitingPeriod();
    }
}