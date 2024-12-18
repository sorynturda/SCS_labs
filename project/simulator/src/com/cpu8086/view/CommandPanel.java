package com.cpu8086.view;

import com.cpu8086.controller.CPUController;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CommandPanel extends JPanel {
    private JTextArea instructionArea;
    private JButton executeButton;
    private JButton executeAllButton;
    private JButton clearButton;
    private Consumer<String> commandListener;

    public CommandPanel(CPUController controller) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Commands"));
        initializeComponents();
    }

    private void initializeComponents() {
        instructionArea = new JTextArea(5, 40);
        instructionArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        executeButton = new JButton("Execute Next");
        executeAllButton = new JButton("Execute All");
        clearButton = new JButton("Clear");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(executeButton);
        buttonPanel.add(executeAllButton);
        buttonPanel.add(clearButton);

        add(new JScrollPane(instructionArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        executeButton.addActionListener(e -> executeNext());
        executeAllButton.addActionListener(e -> executeAll());
        clearButton.addActionListener(e -> instructionArea.setText(""));
    }

    private void executeNext() {
        String[] instructions = instructionArea.getText().split("\n");
        for (String instruction : instructions) {
            if (!instruction.trim().isEmpty()) {
                commandListener.accept(instruction.trim());
                break;
            }
        }
    }

    private void executeAll() {
        String[] instructions = instructionArea.getText().split("\n");
        for (String instruction : instructions) {
            if (!instruction.trim().isEmpty()) {
                commandListener.accept(instruction.trim());
            }
        }
    }

    public void setCommandListener(Consumer<String> listener) {
        this.commandListener = listener;
    }

    public void clearInstructions() {
        instructionArea.setText("");
    }
}