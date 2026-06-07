    package GUI;

    import javax.swing.*;
    import java.awt.*;
    import java.util.ArrayList;
    import java.util.List;

    public class SimulationFrame extends JFrame {

        private JPanel serversPanel;
        private JLabel peakLabel;
        private JLabel averageServiceTimeLabel;
        private JLabel timeLabel;
        private List<JPanel> serverQueues;
        private JPanel waitingClientsPanel;
        private JLabel averageTasksWaitingTimeLabel;

        public SimulationFrame(int numberOfServers)
        {
            this.setTitle("Simulation");
            this.setSize(900, 700);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLayout(new BorderLayout());

            serversPanel = new JPanel();
            serversPanel.setLayout(new GridLayout(numberOfServers, 1));

            serverQueues = new ArrayList<>();

            peakLabel = new JLabel("Peak hour: -");
            averageServiceTimeLabel = new JLabel("Average service time:");
            averageTasksWaitingTimeLabel = new JLabel("Average tasks waiting time:");

            for(int i = 0; i < numberOfServers; i++)
            {
                JPanel queuePanel = new JPanel();
                queuePanel.setBorder(BorderFactory.createTitledBorder("Queue " + (i + 1)));
                queuePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                serverQueues.add(queuePanel);
                serversPanel.add(queuePanel);
            }

            this.setLayout(new BorderLayout());
            JPanel topPanel = new JPanel(new GridLayout(2, 1));
            waitingClientsPanel = new JPanel();
            waitingClientsPanel.setBorder(BorderFactory.createTitledBorder("Waiting Clients"));
            waitingClientsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(waitingClientsPanel, BorderLayout.CENTER);
            this.add(new JScrollPane(serversPanel), BorderLayout.CENTER);
            this.setVisible(true);


            timeLabel = new JLabel("Time: 0");
            topPanel.add(timeLabel);
            topPanel.add(peakLabel);
            topPanel.add(averageServiceTimeLabel);
            topPanel.add(averageTasksWaitingTimeLabel);

            this.add(topPanel, BorderLayout.NORTH);
        }


        public void updateQueues(List<List<String>> queues)
        {
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < queues.size(); i++)
                {
                    JPanel queuePanel = serverQueues.get(i);
                    queuePanel.removeAll();

                    for (String task : queues.get(i))
                    {
                        JLabel label = new JLabel(task);

                        label.setOpaque(true);
                        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                        queuePanel.add(label);
                    }

                    queuePanel.revalidate();
                    queuePanel.repaint();
                }
            });
        }

        public void updateWaitingClients(List<String> clients)
        {
            SwingUtilities.invokeLater(() -> {

                waitingClientsPanel.removeAll();

                for (String c : clients)
                {
                    JLabel label = new JLabel(c);

                    label.setOpaque(true);
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    waitingClientsPanel.add(label);
                }

                waitingClientsPanel.revalidate();
                waitingClientsPanel.repaint();
            });
        }

        public void setTime(int time)
        {
            SwingUtilities.invokeLater(() -> {
                timeLabel.setText("Time: " + time);
            });
        }

        public void showPeakHour(int peakTime, int clients)
        {
            SwingUtilities.invokeLater(() -> {
                peakLabel.setText("Peak hour: " + peakTime + " | clients: " + clients);
            });
        }

        public void showAverageServiceTime(float averageServiceTime)
        {
            SwingUtilities.invokeLater(() -> {
                averageServiceTimeLabel.setText("Average service time:" + averageServiceTime);
            });
        }

        public void showAverageTasksWaitingTime(float tasksAverageWaitingTime)
        {
            SwingUtilities.invokeLater(() -> {
                averageTasksWaitingTimeLabel.setText("Average tasks waiting time in queue:" + tasksAverageWaitingTime);
            });
        }
    }