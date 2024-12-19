package cpu8086.view;

import cpu8086.controller.CPUController;
import cpu8086.exception.CPUException;
import cpu8086.exception.InvalidOperandException;

import javax.swing.*;
import java.awt.*;

public class CommandPanel extends JPanel {
    private JTextArea programArea;
    private JButton loadButton;
    private JButton stepButton;
    private JButton runButton;
    private JButton resetButton;
    private JLabel statusLabel;
    private CPUController controller;
    private SimulatorGUI parent;

    public CommandPanel(CPUController controller, SimulatorGUI parent) {
        this.controller = controller;
        this.parent = parent;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Program Control"));
        initializeComponents();
    }

    private void initializeComponents() {
        programArea = new JTextArea(10, 40);
        programArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loadButton = new JButton("Load Program");
        stepButton = new JButton("Step");
        runButton = new JButton("Run");
        resetButton = new JButton("Reset");
        statusLabel = new JLabel("Ready");

        buttonPanel.add(loadButton);
        buttonPanel.add(stepButton);
        buttonPanel.add(runButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(statusLabel);

        add(new JScrollPane(programArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> loadProgram());
        stepButton.addActionListener(e -> executeStep());
        runButton.addActionListener(e -> executeAll());
        resetButton.addActionListener(e -> reset());

        updateButtonStates();
    }

    private void loadProgram() {
        try {
            String program = programArea.getText();
            String[] lines = program.split("\n");

            // Pre-validate all instructions before loading
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                if (!line.isEmpty()) {
                    try {
                        controller.getDecoder().decode(line);
                    } catch (InvalidOperandException e) {
                        throw new InvalidOperandException("Error at line " + (i + 1) + ": " + e.getMessage());
                    }
                }
            }

            // Reset CPU state before loading new program
            controller.reset();

            // If validation passed, load the program
            controller.loadProgram(program);
            statusLabel.setText("Program loaded successfully");
            updateButtonStates();
            parent.updateDisplays();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void executeStep() {
        try {
            if (controller.hasMoreInstructions()) {
                String currentInstruction = controller.getCurrentInstruction();
                try {
                    controller.executeNextStep();
                    statusLabel.setText("Current: " + currentInstruction);
                    updateButtonStates();
                    parent.updateDisplays();
                } catch (CPUException e) {
                    showError("Error executing instruction '" + currentInstruction + "': " + e.getMessage());
                }
            } else {
                statusLabel.setText("Program completed");
            }
        } catch (Exception e) {
            showError("Execution error: " + e.getMessage());
        }
    }

    private void executeAll() {
        try {
            controller.executeAll();
            statusLabel.setText("Program completed");
            updateButtonStates();
            parent.updateDisplays();
        } catch (CPUException e) {
            showError("Execution error: " + e.getMessage());
        }
    }

    private void reset() {
        controller.reset();
        statusLabel.setText("Ready");
        updateButtonStates();
        parent.updateDisplays();
    }

    private void updateButtonStates() {
        boolean hasProgram = !programArea.getText().trim().isEmpty();
        boolean hasMoreInstructions = controller.hasMoreInstructions();

        stepButton.setEnabled(hasProgram && hasMoreInstructions);
        runButton.setEnabled(hasProgram && hasMoreInstructions);
        resetButton.setEnabled(hasProgram);
    }

    private void showError(String message) {
        statusLabel.setText("Error");
        JOptionPane.showMessageDialog(this,
                message,
                "Execution Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void updateDisplay() {
        updateButtonStates();
        if (!controller.hasMoreInstructions()) {
            statusLabel.setText("Program completed");
        }
    }
}