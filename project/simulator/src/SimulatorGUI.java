import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class SimulatorGUI extends JFrame {
    private Intel8086Simulator simulator;
    private JTextArea memoryArea;
    private JTextArea registersArea;
    private JTextArea flagsArea;
    private JTextField commandField;

    public SimulatorGUI() {
        simulator = new Intel8086Simulator();

        setTitle("Intel 8086 Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create display areas
        memoryArea = new JTextArea(20, 30);
        registersArea = new JTextArea(10, 30);
        flagsArea = new JTextArea(5, 30);

        memoryArea.setEditable(false);
        registersArea.setEditable(false);
        flagsArea.setEditable(false);

        // Create command input
        JPanel commandPanel = new JPanel();
        commandField = new JTextField(30);
        JButton executeButton = new JButton("Execute");

        executeButton.addActionListener(e -> executeCommand());
        commandField.addActionListener(e -> executeCommand());

        // Add help button

        commandPanel.add(new JLabel("Command:"));
        commandPanel.add(commandField);
        commandPanel.add(executeButton);

        // Layout
        JPanel displayPanel = new JPanel(new GridLayout(3, 1));
        displayPanel.add(new JScrollPane(registersArea));
        displayPanel.add(new JScrollPane(flagsArea));
        displayPanel.add(new JScrollPane(memoryArea));

        add(displayPanel, BorderLayout.CENTER);
        add(commandPanel, BorderLayout.SOUTH);

        // Initial update
        updateDisplay();

        pack();
        setLocationRelativeTo(null);
    }

    private void executeCommand() {
        String command = commandField.getText().trim().toUpperCase();
        String[] parts = command.split(" ", 2);

        if (parts.length < 1) {
            JOptionPane.showMessageDialog(this, "Invalid command format");
            return;
        }

        try {
            switch (parts[0]) {
                case "MOV":
                    String[] operands = parts[1].split(",");
                    simulator.mov(operands[0].trim(), operands[1].trim());
                    break;

                case "ADD":
                    operands = parts[1].split(",");
                    simulator.add(operands[0].trim(), operands[1].trim());
                    break;

                case "SUB":
                    operands = parts[1].split(",");
                    simulator.sub(operands[0].trim(), operands[1].trim());
                    break;

                case "OR":
                    operands = parts[1].split(",");
                    simulator.or(operands[0].trim(), operands[1].trim());
                    break;

                case "XOR":
                    operands = parts[1].split(",");
                    simulator.xor(operands[0].trim(), operands[1].trim());
                    break;

                case "AND":
                    operands = parts[1].split(",");
                    simulator.and(operands[0].trim(), operands[1].trim());
                    break;

                case "MUL":
                    simulator.mul(parts[1].trim());
                    break;

                case "JMP":
                    simulator.jmp(Integer.parseInt(parts[1].trim()));
                    break;

                case "CMP":
                    operands = parts[1].split(",");
                    simulator.cmp(operands[0].trim(), operands[1].trim());
                    break;

                case "INC":
                    simulator.inc(parts[1].trim());
                    break;

                case "DEC":
                    simulator.dec(parts[1].trim());
                    break;

                case "SHR":
                    operands = parts[1].split(",");
                    simulator.shr(operands[0].trim(), operands[1].trim());
                    break;

                case "JZ":
                    simulator.jz(Integer.parseInt(parts[1].trim()));
                    break;

                case "JG":
                    simulator.jg(Integer.parseInt(parts[1].trim()));
                    break;

                case "SHL":
                    operands = parts[1].split(",");
                    simulator.shr(operands[0].trim(), operands[1].trim());
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Unknown command: " + parts[0]);
                    return;
            }

            updateDisplay();
            commandField.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error executing command: " + e.getMessage());
        }
    }

    private void showHelp() {
        String helpText =
                "Available Commands:\n\n" +
                        "1. MOV dest,src - Move data\n" +
                        "2. ADD dest,src - Add\n" +
                        "3. SUB dest,src - Subtract\n" +
                        "4. OR dest,src - Logical OR\n" +
                        "5. XOR dest,src - Logical XOR\n" +
                        "6. AND dest,src - Logical AND\n" +
                        "7. MUL src - Multiply AX with src\n" +
                        "8. JMP address - Jump to address\n" +
                        "9. CMP op1,op2 - Compare\n" +
                        "10. INC dest - Increment\n" +
                        "11. DEC dest - Decrement\n" +
                        "12. SHR dest,count - Shift right\n" +
                        "13. JZ address - Jump if zero\n" +
                        "14. JG address - Jump if greater\n" +
                        "15. LOOP address - Loop with CX\n\n" +
                        "Available Registers: AX, BX, CX, DX, SI, DI, SP, BP\n\n" +
                        "Examples:\n" +
                        "MOV AX,5\n" +
                        "ADD BX,AX\n" +
                        "MUL BX\n" +
                        "JMP 100";

        JTextArea textArea = new JTextArea(helpText);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateDisplay() {
        // Update registers display
        StringBuilder regs = new StringBuilder("Registers:\n");
        for (Map.Entry<String, Integer> entry : simulator.getRegisters().entrySet()) {
            regs.append(String.format("%s: %04X (%d)\n",
                    entry.getKey(), entry.getValue(), entry.getValue()));
        }
        regs.append(String.format("IP: %04X (%d)\n", simulator.getIP(), simulator.getIP()));
        registersArea.setText(regs.toString());

        // Update flags display
        StringBuilder flags = new StringBuilder("Flags:\n");
        for (Map.Entry<String, Boolean> entry : simulator.getFlags().entrySet()) {
            flags.append(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
        }
        flagsArea.setText(flags.toString());

        // Update memory display
        StringBuilder mem = new StringBuilder("Memory (first 256 bytes):\n");
        int[] memory = simulator.getMemory();
        for (int i = 0; i < 256; i += 16) {
            mem.append(String.format("%04X: ", i));
            for (int j = 0; j < 16; j++) {
                mem.append(String.format("%02X ", memory[i + j]));
            }
            mem.append("\n");
        }
        memoryArea.setText(mem.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimulatorGUI().setVisible(true);
        });
    }
}