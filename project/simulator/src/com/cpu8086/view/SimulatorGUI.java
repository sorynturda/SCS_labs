package com.cpu8086.view;

import com.cpu8086.controller.CPUController;

import javax.swing.*;
import java.awt.*;

public class SimulatorGUI extends JFrame {
    private CPUController controller;
    private MemoryPanel memoryPanel;
    private RegisterPanel registerPanel;
    private FlagPanel flagPanel;
    private JTextArea aluDisplay;
    private CommandPanel commandPanel;

    public SimulatorGUI() {
        controller = new CPUController();
        setupUI();
        updateDisplays();
    }

    private void setupUI() {
        setTitle("8086 CPU Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // Create main display panel with grid layout
        JPanel displayPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        // Initialize panels
        memoryPanel = new MemoryPanel(controller);
        registerPanel = new RegisterPanel(controller);
        flagPanel = new FlagPanel(controller);

        // ALU display
        aluDisplay = createDisplayArea("ALU Status");

        // Add panels to display panel
        displayPanel.add(new JScrollPane(memoryPanel));
        displayPanel.add(new JScrollPane(registerPanel));
        displayPanel.add(new JScrollPane(flagPanel));
        displayPanel.add(createScrollPane(aluDisplay, "ALU"));

        // Command panel - now passing 'this' reference
        commandPanel = new CommandPanel(controller, this);

        // Add components to frame
        add(displayPanel, BorderLayout.CENTER);
        add(commandPanel, BorderLayout.SOUTH);

        // Set frame properties
        setSize(900, 800);
        setLocationRelativeTo(null);
    }

    private JTextArea createDisplayArea(String name) {
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);
        return area;
    }

    private JScrollPane createScrollPane(JTextArea area, String title) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createTitledBorder(title));
        return scroll;
    }

    public void updateDisplays() {
        memoryPanel.update();
        registerPanel.update();
        flagPanel.update();
        updateALUDisplay();
        commandPanel.updateDisplay();
    }

    private void updateALUDisplay() {
        StringBuilder aluText = new StringBuilder();
        aluText.append("Available Instructions:\n");
        aluText.append("-".repeat(30)).append("\n");
        aluText.append("Data Movement:\n");
        aluText.append("  MOV dest, src    - Move data to destination\n\n");

        aluText.append("Arithmetic:\n");
        aluText.append("  ADD dest, src    - Add source to destination\n");
        aluText.append("  SUB dest, src    - Subtract source from destination\n");
        aluText.append("  MUL src         - Multiply AX by source\n");
        aluText.append("  INC dest        - Increment by 1\n");
        aluText.append("  DEC dest        - Decrement by 1\n\n");

        aluText.append("Logical:\n");
        aluText.append("  AND dest, src    - Logical AND\n");
        aluText.append("  OR  dest, src    - Logical OR\n");
        aluText.append("  XOR dest, src    - Logical XOR\n");
        aluText.append("  SHR dest, count  - Shift right\n\n");

        aluText.append("Control Flow:\n");
        aluText.append("  JMP target      - Unconditional jump\n");
        aluText.append("  CMP dest, src    - Compare operands\n");
        aluText.append("  JZ  target      - Jump if zero\n");
        aluText.append("  JG  target      - Jump if greater\n");
        aluText.append("  LOOP target     - Decrement CX and jump if CX≠0\n\n");

        aluDisplay.setText(aluText.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimulatorGUI().setVisible(true);
        });
    }
}