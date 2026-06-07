package GUI;

import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;

import javax.swing.*;
import java.awt.*;

public class InputFrame extends JFrame{

    public InputFrame ()
    {
        this.setSize(850, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(10, 2));

        JButton startSimulationButton = new JButton("Start simulation!");


        JTextField timeLimitField = new JTextField();
        JTextField maxProcessingTimeField = new JTextField();
        JTextField minProcessingTimeField = new JTextField();
        JTextField numberOfServersField = new JTextField();
        JTextField numberOfClientsField = new JTextField();
        JComboBox<SelectionPolicy> selectionPolicyBox = new JComboBox<>(SelectionPolicy.values());
        JTextField maxArrivalTimeField= new JTextField();
        JTextField minArrivalTimeField = new JTextField();

        SelectionPolicy policy = (SelectionPolicy) selectionPolicyBox.getSelectedItem();

        panel.add(new JLabel("Time limit:"));
        panel.add(timeLimitField);

        panel.add(new JLabel("Max Service Time:"));
        panel.add(maxProcessingTimeField);

        panel.add(new JLabel("Min Service Time"));
        panel.add(minProcessingTimeField);

        panel.add(new JLabel("Number of Servers:"));
        panel.add(numberOfServersField);

        panel.add(new JLabel("Number of Clients:"));
        panel.add(numberOfClientsField);
        panel.add(new JLabel("Selection Policy:"));
        panel.add(selectionPolicyBox);

        panel.add(new JLabel("Max Arrival Time:"));
        panel.add(maxArrivalTimeField);

        panel.add(new JLabel("Min Arrival Time:"));
        panel.add(minArrivalTimeField);

        panel.add(startSimulationButton);

        startSimulationButton.addActionListener(e -> {
            try
            {
                int timeLimit = Integer.parseInt(timeLimitField.getText());
                int maxProcessingTime = Integer.parseInt(maxProcessingTimeField.getText());
                int minProcessingTime = Integer.parseInt(minProcessingTimeField.getText());
                int numberOfServers = Integer.parseInt(numberOfServersField.getText());
                int numberOfClients = Integer.parseInt(numberOfClientsField.getText());
                int maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());
                int minArrivalTime = Integer.parseInt(minArrivalTimeField.getText());

                SimulationFrame simFrame = new SimulationFrame(numberOfServers);
                SimulationManager manager = new SimulationManager(timeLimit, maxProcessingTime, minProcessingTime, numberOfServers, numberOfClients, policy, simFrame, minArrivalTime, maxArrivalTime);
                new Thread(manager).start();
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(null, "Values are not valid");
            }
        });

        this.add(panel);
        this.setVisible(true);
    }

}
