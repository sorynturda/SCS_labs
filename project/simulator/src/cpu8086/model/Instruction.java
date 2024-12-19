package cpu8086.model;

import java.util.*;

public class Instruction {
    private final InstructionType type;
    private final String[] operands;
    private final int size;
    private final Set<RegisterType> sourceRegisters;
    private final Set<RegisterType> destinationRegisters;
    private boolean requiresMemoryAccess;

    public Instruction(InstructionType type, String[] operands) {
        this.type = type;
        this.operands = operands.clone();
        this.sourceRegisters = new HashSet<>();
        this.destinationRegisters = new HashSet<>();
        this.size = calculateSize();
        analyzeOperands();
    }

    private int calculateSize() {
        // Calculate instruction size based on type and operands
        switch (type) {
            case MOV:
            case ADD:
            case SUB:
            case AND:
            case OR:
            case XOR:
                return operands.length == 2 ? 2 : 3;
            case JMP:
            case JZ:
            case JG:
            case LOOP:
                return 2;
            case INC:
            case DEC:
                return 1;
            default:
                return 1;
        }
    }

    private void analyzeOperands() {
        // Analyze operands to determine register usage and memory access
        for (int i = 0; i < operands.length; i++) {
            String operand = operands[i].trim().toUpperCase();

            // Check for memory access
            if (operand.startsWith("[") && operand.endsWith("]")) {
                requiresMemoryAccess = true;
                continue;
            }

            // Check for register usage
            try {
                RegisterType reg = RegisterType.valueOf(operand);
                if (i == 0 && isDestinationFirst()) {
                    destinationRegisters.add(reg);
                } else {
                    sourceRegisters.add(reg);
                }
            } catch (IllegalArgumentException ignored) {
                // Not a register - might be immediate value
            }
        }

    }

    private boolean isDestinationFirst() {
        return type != InstructionType.MUL;
    }


    public InstructionType getType() {
        return type;
    }

    public String[] getOperands() {
        return operands.clone();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        if (operands.length > 0) {
            sb.append(" ").append(String.join(", ", operands));
        }
        return sb.toString();
    }
}