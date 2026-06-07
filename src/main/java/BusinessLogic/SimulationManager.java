package BusinessLogic;
import GUI.*;
import Model.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public class SimulationManager implements Runnable{
    private Scheduler scheduler;
    private SimulationFrame frame;
    private ArrayList<Task> generatedTasks;;
    private ExecutorService pool;

    private int timeLimit;
    private int maxProcessingTime ;
    private int minProcessingTime;
    private int numberOfServers;
    private int numberOfClients;
    private SelectionPolicy selectionPolicy;
    private int minArrivalTime;
    private int maxArrivalTime;

    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime, int numberOfServers, int numberOfClients, SelectionPolicy selectionPolicy, SimulationFrame simulationFrame, int minArrivalTime, int maxArrivalTime)
    {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.selectionPolicy = selectionPolicy;
        this.frame = simulationFrame;
        this.maxArrivalTime = maxArrivalTime;
        this.minArrivalTime = minArrivalTime;

        this.scheduler = new Scheduler(this.numberOfServers, 100);
        this.scheduler.changeStrategy(this.selectionPolicy);
        this.generatedTasks = new ArrayList<>(numberOfClients);
        generateNRandomTasks();
        this.pool = Executors.newFixedThreadPool(numberOfServers);
        for(Server server : this.scheduler.getServers())
        {
            pool.execute(server);
        }
    }

    private void writeToFile(String text)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tests.txt", true)))
        {
            writer.write(text);
            writer.newLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void generateNRandomTasks()
    {
        int randomProcessingTime;
        int randomArrivalTime;
        for(int i = 0; i < numberOfClients; i++)
        {
            randomProcessingTime = ThreadLocalRandom.current().nextInt(minProcessingTime, maxProcessingTime+1);
            randomArrivalTime = ThreadLocalRandom.current().nextInt(minArrivalTime, maxArrivalTime);
            this.generatedTasks.add(new Task(randomArrivalTime, randomProcessingTime));
        }
        generatedTasks.sort(Comparator.comparing(Task::getArrivalTime));
    }

    public ArrayList<Task> getGeneratedTasks()
    {
        return this.generatedTasks;
    }

    @Override
    public void run()
    {
        int currentTime = 0;
        boolean finishedEarly = false;
        int peakTimeHour = 0;
        int peakTimeNumberOfClients = 0;
        float averageWaitingTime = 0;
        float numberTasksWaitingTime = 0;
        float totalAverageServiceTime = 0;

        String line = "N = " + this.numberOfClients + " , Q = " + this.numberOfServers + " , maxTime =" + this.timeLimit + " , min Arrival Time: " + this.minArrivalTime + " , max Arrival time: " + this.maxArrivalTime + " ,max Service time: " + this.maxProcessingTime + " , min Service time: " + this.minProcessingTime;
        writeToFile(line);
        while(currentTime <= timeLimit)
        {
            frame.setTime(currentTime);
            System.out.println();
            System.out.println();
            System.out.println();
            boolean allTasksDone = true;
            if(!this.generatedTasks.isEmpty())
            {
                allTasksDone = false;
            }

            for(Server server : this.scheduler.getServers())
            {
                if(!server.getTasks().isEmpty())
                {
                    allTasksDone = false;
                }
            }

            if(allTasksDone == true)
            {
                finishedEarly = true;
                for(Server server : this.scheduler.getServers())
                {
                    totalAverageServiceTime += server.getAverageServerWaitingTime();
                }
                totalAverageServiceTime = Math.abs(totalAverageServiceTime / this.scheduler.getServers().size());

                averageWaitingTime = Math.abs(averageWaitingTime / numberTasksWaitingTime);

                for(Server server : this.scheduler.getServers())
                {
                    server.stop();
                }

                pool.shutdown();
                try
                {
                    if (!pool.awaitTermination(5, TimeUnit.SECONDS))
                    {
                        pool.shutdownNow();
                    }
                }
                catch (InterruptedException e)
                {
                    pool.shutdownNow();
                }
                frame.showAverageServiceTime(totalAverageServiceTime);
                frame.showPeakHour(peakTimeHour, peakTimeNumberOfClients);
                frame.showAverageTasksWaitingTime(averageWaitingTime);

                System.out.println("Peak hour - Peak time: " + peakTimeHour + " with number of clients: " + peakTimeNumberOfClients);
                line = "Peak hour - Peak time: " + peakTimeHour + " with number of clients: " + peakTimeNumberOfClients;
                writeToFile(line);
                System.out.println("Average service time:" + totalAverageServiceTime);
                line = "Average service time:" + totalAverageServiceTime;
                writeToFile(line);
                System.out.println("Average waiting time: " + averageWaitingTime);
                line = "Average waiting time: " + averageWaitingTime;
                writeToFile(line);
                System.out.println("Simulation ended early successfully at time " + currentTime);
                line = "Simulation ended early successfully at time " + currentTime;
                writeToFile(line);
                break;
            }


            line = "Time: " + currentTime;
            writeToFile(line);

            System.out.println("Time: " + currentTime);
            System.out.println("Waiting clients:");
            line = "Waiting clients: ";
            writeToFile(line);

            for(Task task : generatedTasks)
            {
                System.out.printf("(%d, %d, %d), ", task.getID(), task.getArrivalTime(), task.getServiceTime());
                line = "(" + task.getID() +" ," + task.getArrivalTime() + " ," + task.getServiceTime() + ")";
                writeToFile(line);
            }
            System.out.println();

            while(!generatedTasks.isEmpty() && generatedTasks.get(0).getArrivalTime() <= currentTime)
            {
                Task task = generatedTasks.remove(0);
                scheduler.dispatchTask(task);
                averageWaitingTime += task.getWaitingTime();
                numberTasksWaitingTime++;
            }

            int comparePeakTimeNumberOfClients = 0;
            for(Server server : this.scheduler.getServers())
            {
                comparePeakTimeNumberOfClients += server.getTasks().size();
            }
            if(comparePeakTimeNumberOfClients >= peakTimeNumberOfClients)
            {
                peakTimeNumberOfClients = comparePeakTimeNumberOfClients;
                peakTimeHour = currentTime;
            }


            int serverNr = 1;
            for(Server server : this.scheduler.getServers())
            {
                BlockingQueue<Task> queue = server.getTasks();
                if(queue.isEmpty())
                {
                    System.out.println("Queue " + serverNr + ": closed");
                    line = "Queue " + serverNr + ": closed";
                    writeToFile(line);
                }
                else
                {
                    System.out.println("Queue " + serverNr);
                    line = "Queue" + serverNr;
                    writeToFile(line);
                    for(Task task : queue)
                    {
                        System.out.printf("(%d, %d, %d) ", task.getID(), task.getArrivalTime(), task.getServiceTime());
                        line = "(" + task.getID() +" ," + task.getArrivalTime() + " ," + task.getServiceTime() + ")";
                        writeToFile(line);
                        System.out.println();
                    }
                }
                serverNr++;
            }

            List<List<String>> display = new ArrayList<>();
            for (Server server : this.scheduler.getServers())
            {
                List<String> queue = new ArrayList<>();
                for (Task task : server.getTasks())
                {
                    queue.add("(" + task.getID() + ", " + task.getArrivalTime() + ", " + task.getServiceTime() + ")");
                }

                display.add(queue);
            }
            frame.updateQueues(display);

            List<String> waiting = new ArrayList<>();
            for (Task task : generatedTasks)
            {
                waiting.add("(" + task.getID() + ", " + task.getArrivalTime() + ", " + task.getServiceTime() + ")");
            }
            frame.updateWaitingClients(waiting);


            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }

            currentTime++;
        }

        averageWaitingTime = Math.abs(averageWaitingTime / numberTasksWaitingTime);

        frame.showPeakHour(peakTimeHour, peakTimeNumberOfClients);

        for(Server server : this.scheduler.getServers())
        {
            totalAverageServiceTime += server.getAverageServerWaitingTime();
        }
        totalAverageServiceTime = totalAverageServiceTime / this.scheduler.getServers().size();

        frame.showAverageServiceTime(totalAverageServiceTime);
        frame.showAverageTasksWaitingTime(averageWaitingTime);

        if(finishedEarly == true)
        {
            return;
        }

        System.out.println("Simulation time limit reached, waiting for all tasks to complete");
        line = "Simulation time limit reached, waiting for all tasks to complete";
        writeToFile(line);
        boolean allTasksDone = false;
        int waitCounter = 0;
        while(!allTasksDone && waitCounter < 30)
        {
            allTasksDone= true;
            for(Server server : this.scheduler.getServers())
            {
                if(!server.getTasks().isEmpty())
                {
                    allTasksDone = false;
                    break;
                }
            }

            if(!allTasksDone)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            waitCounter++;
        }

        System.out.println("All tasks completed!");
        line = "All tasks completed!";
        writeToFile(line);

        for(Server server : this.scheduler.getServers())
        {
            server.stop();
        }

        pool.shutdown();
        try
        {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS))
            {
                pool.shutdownNow();
            }
        }
        catch (InterruptedException e)
        {
            pool.shutdownNow();
        }
        System.out.println("Peak hour - Peak time: " + peakTimeHour + " with number of clients: " + peakTimeNumberOfClients);
        System.out.println("Average service time: " + totalAverageServiceTime);
        System.out.println("Average waiting time: " + averageWaitingTime);
        System.out.println("Simulation ended successfully!");
        line = "Peak hour - Peak time: " + peakTimeHour + " with number of clients: " + peakTimeNumberOfClients;
        writeToFile(line);
        line = "Average service time: " + totalAverageServiceTime;
        writeToFile(line);
        line = "Average waiting time: " + averageWaitingTime;
        writeToFile(line);
        line = "Simulation ended successfully!";
        writeToFile(line);
        line = "------------- ";
        writeToFile(line);
        line = "------------- ";
        writeToFile(line);

    }
}