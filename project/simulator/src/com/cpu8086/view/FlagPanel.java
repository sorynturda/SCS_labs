
// view/FlagPanel.java
package com.cpu8086.view;

import com.cpu8086.controller.CPUController;
import com.cpu8086.model.FlagRegister;

import javax.swing.*;
import java.awt.*;

public class FlagPanel extends JPanel {
    private CPUController controller;
    private JCheckBox[] flagBoxes;
    private JLabel[] flagLabels;
    private static final String[] FLAG_NAMES = {
        "CF (Carry)",
        "PF (Parity)",
        "AF (Auxiliary)",
        "ZF (Zero)",
        "SF (Sign)",
        "OF (Overflow)"
    };
    private static final String[] FLAG_DESCRIPTIONS = {
        "Set if operation resulted in a carry/borrow",
        "Set if result has even number of 1 bits",
        "Set if carry from bit 3 to bit 4",
        "Set if result is zero",
        "Set if result is negative (MSB=1)",
        "Set if signed operation overflow"
    };

    public FlagPanel(CPUController controller) {
        this.controller = controller;
        setLayout(new GridLayout(FLAG_NAMES.length, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Flags Status"));
        initializeComponents();
    }

    private void initializeComponents() {
        flagBoxes = new JCheckBox[FLAG_NAMES.length];
        flagLabels = new JLabel[FLAG_NAMES.length];

        for (int i = 0; i < FLAG_NAMES.length; i++) {
            JPanel flagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            flagBoxes[i] = new JCheckBox(FLAG_NAMES[i]);
            flagBoxes[i].setEnabled(false);
            flagBoxes[i].setToolTipText(FLAG_DESCRIPTIONS[i]);

            flagLabels[i] = new JLabel("0");
            flagLabels[i].setPreferredSize(new Dimension(20, 20));

            flagPanel.add(flagBoxes[i]);
            flagPanel.add(flagLabels[i]);

            add(flagPanel);
        }
    }

    public void update() {
        FlagRegister flags = controller.getCPU().getFlags();

        updateFlag(0, flags.isCarryFlag());
        updateFlag(1, flags.isParityFlag());
        updateFlag(2, flags.isAuxiliaryFlag());
        updateFlag(3, flags.isZeroFlag());
        updateFlag(4, flags.isSignFlag());
        updateFlag(5, flags.isOverflowFlag());
    }

    private void updateFlag(int index, boolean value) {
        flagBoxes[index].setSelected(value);
        flagLabels[index].setText(value ? "1" : "0");
        // Use different colors for set/unset flags
        flagBoxes[index].setForeground(value ? new Color(0, 128, 0) : Color.RED);
        flagLabels[index].setForeground(value ? new Color(0, 128, 0) : Color.RED);
    }
}