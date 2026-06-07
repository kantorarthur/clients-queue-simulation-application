package Model;

public class Task {
    private static int nextID = 0;
    private int ID;
    private int arrivalTime;
    private int serviceTime;
    private float waitingTime;
    public Task(int arrivalTime, int serviceTime)
    {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.ID = ++nextID;
    }

    public int getNextID()
    {
        return nextID;
    }

    public int getID()
    {
        return this.ID;
    }

    public float getWaitingTime()
    {
        return this.waitingTime;
    }

    public void setWaitingTime(int waitingTime)
    {
        this.waitingTime = waitingTime;
    }

    public int getArrivalTime()
    {
        return this.arrivalTime;
    }

    public int getServiceTime()
    {
        return this.serviceTime;
    }

    public void setServiceTime(int serviceTime)
    {
        this.serviceTime = serviceTime;
    }

    public void setArrivalTime(int arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString()
    {
        return "Task ID: " + this.getID() + " with arrival time: " + this.getArrivalTime() + " and service time: "
                + this.getServiceTime();
    }

}
