package BusinessLogic;

import Model.*;
import java.util.List;

public class ShortestTimeStrategy implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task task)
    {
        if(servers == null || servers.isEmpty())
            return;

        int minWaitingTime = Integer.MAX_VALUE;
        Server tempServer = null;

        for(Server server : servers)
        {
            int waitingTime = server.getWaitingPeriod().get();
            if(waitingTime < minWaitingTime)
            {
                minWaitingTime = waitingTime;
                tempServer = server;
            }
        }

        if(tempServer != null)
        {
            if(tempServer.getWaitingPeriod().get() != 0)
            {
                task.setWaitingTime(tempServer.getWaitingPeriod().get());
            }
            tempServer.addTask(task);
        }
    }
}