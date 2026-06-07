package BusinessLogic;

import Model.*;
import java.util.List;

public class ShortestQueueStrategy implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task task)
    {
        if(servers == null || servers.isEmpty())
            return;

        int minQueueSize = Integer.MAX_VALUE;
        Server tempServer = null;

        for (Server server : servers)
        {
            int queueSize = server.getTasks().size();
            if(queueSize < minQueueSize)
            {
                minQueueSize = queueSize;
                tempServer = server;
            }
        }

        if (tempServer != null)
        {
            if(tempServer.getWaitingPeriod().get() != 0)
            {
                task.setWaitingTime(tempServer.getWaitingPeriod().get());
            }
            tempServer.addTask(task);
        }
    }
}