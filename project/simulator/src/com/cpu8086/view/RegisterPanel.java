package com.cpu8086.view;

import com.cpu8086.controller.CPUController;
import com.cpu8086.model.RegisterType;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private CPUController controller;
    private JTextField[] registerHexFields;
    private JTextField[] registerDecFields;
    private JLabel[] registerLabels;

    private static final int MAX_SIGNED_16BIT = 32767;    // 0x7FFF
    private static final int MIN_SIGNED_16BIT = -32768;   // 0x8000

    public RegisterPanel(CPUController controller) {
        this.controller = controller;
        setLayout(new GridLayout(0, 3, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Registers"));
        initializeComponents();
    }

    private void initializeComponents() {
        registerHexFields = new JTextField[RegisterType.values().length];
        registerDecFields = new JTextField[RegisterType.values().length];
        registerLabels = new JLabel[RegisterType.values().length];

        // Add header labels
        add(new JLabel("Register"));
        add(new JLabel("Hex"));
        add(new JLabel("Decimal"));

        // Create fields for each register
        for (RegisterType reg : RegisterType.values()) {
            registerLabels[reg.ordinal()] = new JLabel(reg.name() + ":");
            registerHexFields[reg.ordinal()] = new JTextField(4);
            registerDecFields[reg.ordinal()] = new JTextField(6);

            registerHexFields[reg.ordinal()].setEditable(false);
            registerDecFields[reg.ordinal()].setEditable(false);

            // Use monospaced font for better alignment
            registerHexFields[reg.ordinal()].setFont(new Font("Monospaced", Font.PLAIN, 12));
            registerDecFields[reg.ordinal()].setFont(new Font("Monospaced", Font.PLAIN, 12));

            add(registerLabels[reg.ordinal()]);
            add(registerHexFields[reg.ordinal()]);
            add(registerDecFields[reg.ordinal()]);
        }
    }

    public void update() {
        for (RegisterType reg : RegisterType.values()) {
            int value = controller.getCPU().getRegisters().getRegister(reg);

            // Convert to signed value if necessary
            int signedValue = convertToSigned16Bit(value);

            // Clamp the value to valid signed 16-bit range
            signedValue = Math.min(Math.max(signedValue, MIN_SIGNED_16BIT), MAX_SIGNED_16BIT);

            // Display both hex and decimal representations
            registerHexFields[reg.ordinal()].setText(String.format("%04X", value & 0xFFFF));
            registerDecFields[reg.ordinal()].setText(String.format("%d", signedValue));

            // Use different colors for positive and negative values
            Color textColor = (signedValue < 0) ? Color.RED : new Color(0, 128, 0);
            registerDecFields[reg.ordinal()].setForeground(textColor);
        }
    }

    private int convertToSigned16Bit(int value) {
        // Mask to 16 bits and check if the sign bit (bit 15) is set
        value &= 0xFFFF;
        if ((value & 0x8000) != 0) {
            // If sign bit is set, extend the sign bit
            value |= ~0xFFFF;
        }
        return value;
    }
}