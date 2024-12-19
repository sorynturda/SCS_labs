
// Updated CommandPanel.java
package com.cpu8086.view;

import com.cpu8086.controller.CPUController;
import com.cpu8086.exception.CPUException;

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
        // Program text area
        programArea = new JTextArea(10, 40);
        programArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Control buttons
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

        // Add components
        add(new JScrollPane(programArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button listeners
        loadButton.addActionListener(e -> loadProgram());
        stepButton.addActionListener(e -> executeStep());
        runButton.addActionListener(e -> executeAll());
        resetButton.addActionListener(e -> reset());

        updateButtonStates();
    }

    private void loadProgram() {
        try {
            String program = programArea.getText();
            controller.loadProgram(program);
            statusLabel.setText("Program loaded");
            updateButtonStates();
            parent.updateDisplays();
        } catch (Exception e) {
            showError("Error loading program: " + e.getMessage());
        }
    }

    private void executeStep() {
        try {
            if (controller.hasMoreInstructions()) {
                controller.executeNextStep();
                statusLabel.setText("Line " + (controller.getCurrentLine() + 1));
                updateButtonStates();
                parent.updateDisplays();
            } else {
                statusLabel.setText("Program completed");
            }
        } catch (CPUException e) {
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
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    public void updateDisplay() {
        // Update program display state
        updateButtonStates();
        if (controller.hasMoreInstructions()) {
            statusLabel.setText("Line " + (controller.getCurrentLine() + 1));
        } else {
            statusLabel.setText("Program completed");
        }
    }
}