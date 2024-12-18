package com.cpu8086.view;

import com.cpu8086.controller.CPUController;
import com.cpu8086.model.Memory;

import javax.swing.*;
import java.awt.*;

public class MemoryPanel extends JPanel {
    private CPUController controller;
    private JTextArea memoryDisplay;
    private JTextField addressField;
    private JButton viewButton;
    private JButton nextButton;
    private JButton prevButton;
    private int currentAddress = 0;
    private static final int DISPLAY_ROWS = 16;
    private static final int BYTES_PER_ROW = 16;

    public MemoryPanel(CPUController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Memory View"));
        initializeComponents();
    }

    private void initializeComponents() {
        // Navigation panel at top
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Address input with label
        navigationPanel.add(new JLabel("Address (hex):"));
        addressField = new JTextField(6);
        addressField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        navigationPanel.add(addressField);

        // Navigation buttons
        viewButton = new JButton("View");
        prevButton = new JButton("← Previous");
        nextButton = new JButton("Next →");

        navigationPanel.add(viewButton);
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);

        // Memory display
        memoryDisplay = new JTextArea(DISPLAY_ROWS + 2, 75);
        memoryDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        memoryDisplay.setEditable(false);

        // Add components to panel
        add(navigationPanel, BorderLayout.NORTH);
        add(new JScrollPane(memoryDisplay), BorderLayout.CENTER);

        // Setup event handlers
        setupEventHandlers();

        // Initial display
        updateMemoryDisplay();
    }

    private void setupEventHandlers() {
        viewButton.addActionListener(e -> {
            try {
                String addressText = addressField.getText().replaceAll("[Hh]$", "");
                currentAddress = Integer.parseInt(addressText, 16);
                updateMemoryDisplay();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid address format. Please enter a hexadecimal value.",
                    "Invalid Address",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        prevButton.addActionListener(e -> {
            currentAddress = Math.max(0, currentAddress - BYTES_PER_ROW * DISPLAY_ROWS);
            updateMemoryDisplay();
            updateAddressField();
        });

        nextButton.addActionListener(e -> {
            currentAddress = Math.min(0xFFFFF - BYTES_PER_ROW * DISPLAY_ROWS,
                                    currentAddress + BYTES_PER_ROW * DISPLAY_ROWS);
            updateMemoryDisplay();
            updateAddressField();
        });

        addressField.addActionListener(e -> viewButton.doClick());
    }

    public void updateMemoryDisplay() {
        StringBuilder display = new StringBuilder();
        Memory memory = controller.getCPU().getMemory();

        // Header
        display.append("       +0 +1 +2 +3 +4 +5 +6 +7 +8 +9 +A +B +C +D +E +F | ASCII\n");
        display.append("----------------------------------------------------------------\n");

        // Memory contents
        for (int row = 0; row < DISPLAY_ROWS; row++) {
            int baseAddress = currentAddress + (row * BYTES_PER_ROW);

            // Address column
            display.append(String.format("%05X | ", baseAddress));

            // Hex values
            StringBuilder ascii = new StringBuilder();
            for (int col = 0; col < BYTES_PER_ROW; col++) {
                int address = baseAddress + col;
                if (address <= 0xFFFFF) {
                    byte value = memory.readByte(address);
                    display.append(String.format("%02X ", value & 0xFF));
                    ascii.append(isPrintable((char)(value & 0xFF)) ? (char)(value & 0xFF) : '.');
                } else {
                    display.append("-- ");
                    ascii.append(" ");
                }
            }

            // ASCII representation
            display.append("| ").append(ascii).append("\n");
        }

        memoryDisplay.setText(display.toString());
    }

    private void updateAddressField() {
        addressField.setText(String.format("%05X", currentAddress));
    }

    private boolean isPrintable(char c) {
        return c >= 32 && c < 127;
    }

    public void update() {
        updateMemoryDisplay();
    }
}