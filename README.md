# Queue Management Simulation

Full-stack Java application. Simulates a multi-server queueing system, managing the distribution of clients to servers based on a user-selected strategy.

A practical way to visualize this is a supermarket scenario: clients wait in lines to be served, while servers act as cash registers that can process only one client at a time.

The application is built using a robust, multi-threaded Java architecture designed to separate business logic from the presentation layer:

* **Java (Core Backend Logic)**: Handles the simulation engine, client generation algorithms, and queue distribution strategies.
* **Multi-Threading**: The simulation runs asynchronously. Each server (queue) operates on its own dedicated background thread, simulating concurrent real-time processing, while a master scheduler thread manages the simulation clock.
* **Java Swing**: Used to build the graphical user interface (GUI), handling user inputs and rendering the real-time visual updates of the queues.
* **FlatLaf**: A modern, clean Look and Feel laf library used to style and design the Swing interface, giving the application a polished, contemporary appearance.

## Selection Policies
The user can choose between two allocation strategies:

•**SHORTEST_QUEUE**: Directs the incoming client to the queue with the fewest clients currently waiting.

•**SHORTEST_TIME**: Directs the incoming client to the queue with the lowest total waiting time.

## How the Application Works

The application automatically generates a user-defined number of clients. Each client is assigned randomized attributes within the boundaries specified by the user in the configuration panel:   

• **Service time**: The total duration required for a server to process and complete the client's request.

• **Arrival time**: The exact simulation second when a client joins one of the server queues (based on the active selection policy).



### Simulation Output & Log File
At the end of the execution, the application generates a structured text file named `test.txt`. This file acts as a complete log of the simulation, capturing the system's state second by second.

1. **Initial Parameters**: At the very beginning, the file records the configuration used for the simulation.
   * *Example*: `N = 52, Q = 5, maxTime = 31, min Arrival Time: 1, max Arrival time: 25, max Service time: 11, min Service time: 1`
2. **Real-Time Snapshots**: For every single second of the simulation clock, the file logs the exact status of all queues, showing which clients are currently waiting or being served.
3. **Final Performance Metrics**: Once the simulation time limit is reached, the application calculates and appends the following key statistics to the bottom of the file:
   * **Average Waiting Time**: The mean time clients spent waiting in lines before service began.
   * **Average Service Time**: The mean duration required to process a client.
   * **Peak Hour**: The exact simulation second when the system experienced the highest congestion (the maximum total number of clients across all queues simultaneously).

## Requirements

- Java 17 or later, **with GUI support** (e.g. `openjdk-17-jdk`, not a `-headless` package)
- Maven 3.6+ (only needed for building from source)
- A graphical desktop session (X11/Wayland)

Dependencies (FlatLaf 3.5.4 and FlatLaf IntelliJ Themes 3.5.4) are bundled in `runApplication.jar` and downloaded automatically by Maven when building.

## Build and run

```bash
# Clone the repository
git clone https://github.com/kantorarthur/clients-queue-simulation-application

# Enter the main directory of the repository
cd clients-queue-simulation-application

# Run the prebuilt jar
java -jar runApplication.jar
```

To build from source:

```bash
sudo apt install openjdk-17-jdk maven
mvn clean package
java -jar target/runApplication.jar
```

## Troubleshooting

If you get `java.awt.HeadlessException`, a headless Java is being used. Select a full one:

```bash
sudo update-alternatives --config java   # choose Java 17 or 21, not a headless-only version
java -version
```


## Main Input Frame
<img width="835" height="587" alt="image" src="https://github.com/user-attachments/assets/50208ba1-fc36-4b8a-9007-9ce05112b446" />

Upon launching the application, the user is presented with a configuration interface to set up the simulation parameters:

• **Time limit**: The total duration (in seconds) for which the simulation will run.

• **Max Service Time**: The maximum service time that a client have to be generated with.

• **Min Service Time**: The minimum service time that a client have to be generated with.

• **Number of servers**: The number of available processing units (cash registers).

• **Number of clients**: Total number of clients to be generated and simulated.

• **Selection Policy**: The algorithm used to dispatch clients to queues (SHORTEST_QUEUE or SHORTEST_TIME).

• **Max Arrival Time**: The maximum arrival time that a client have to be generated with.

• **Min Arrival Time**: The minimum arrival time that a client have to be generated with.

## Simulation Progress Frame
<img width="880" height="685" alt="image" src="https://github.com/user-attachments/assets/7e38649b-cdf3-4c5e-a277-05c7bf01b999" />

Once the simulation starts, this frame provides a real-time visual representation of the system's state. It displays the pool of waiting clients and the current status of each individual server queue.

### Client Representation Format
Every client in the system is represented as a tuple: `(ID, Arrival Time, Service Time)`

* **ID**: A unique identifier assigned to each client upon generation.
* **Arrival Time**: The exact simulation second when the client becomes active and enters a queue.
* **Service Time**: The remaining processing time required for the server to finish serving this specific client. 
  *(Note: For the client currently at the front of a queue, this value decrements by 1 every second until it reaches 0 and the client leaves the system).*


