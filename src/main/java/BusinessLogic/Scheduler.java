package BusinessLogic;
import Model.*;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer)
    {
        this.maxNoServers = maxNoServers;
        this.servers = new ArrayList<>(maxNoServers);
        for(int i = 0; i < maxNoServers ; i++)
        {
            this.servers.add(new Server());
        }
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public List<Server> getServers()
    {
        return this.servers;
    }

    public int getMaxNoServers()
    {
        return this.maxNoServers;
    }

    public int getMaxTasksPerServer()
    {
        return this.maxTasksPerServer;
    }

    public Strategy getStrategy()
    {
        return this.strategy;
    }

    public void setMaxTasksPerServer(int maxTasksPerServer)
    {
        this.maxTasksPerServer = maxTasksPerServer;
    }

    public void setMaxNoServers(int maxNoServers)
    {
        this.maxNoServers = maxNoServers;
    }

    public void changeStrategy(SelectionPolicy policy)
    {
        if(policy == SelectionPolicy.SHORTEST_QUEUE)
        {
            this.strategy = new ShortestQueueStrategy();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME)
        {
            this.strategy = new ShortestTimeStrategy();
        }
    }

    public void dispatchTask(Task task)
    {
        this.strategy.addTask(this.servers,task);
    }
}
