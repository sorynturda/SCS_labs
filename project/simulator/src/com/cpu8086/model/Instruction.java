package com.cpu8086.model;

import java.util.*;

public class Instruction {
    private final InstructionType type;
    private final String[] operands;
    private final int size;
    private final Set<RegisterType> sourceRegisters;
    private final Set<RegisterType> destinationRegisters;
    private boolean requiresMemoryAccess;
    private int memoryAddress;
    private int cycles;

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

        // Calculate base cycles
        cycles = calculateBaseCycles();
    }

    private boolean isDestinationFirst() {
        return type != InstructionType.MUL;
    }

    private int calculateBaseCycles() {
        // Base cycle counts for different instruction types
        switch (type) {
            case MOV:
                return requiresMemoryAccess ? 4 : 1;
            case ADD:
            case SUB:
            case AND:
            case OR:
            case XOR:
                return requiresMemoryAccess ? 3 : 1;
            case MUL:
                return 10;
            case JMP:
            case JZ:
            case JG:
                return 2;
            case LOOP:
                return 5;
            default:
                return 1;
        }
    }

    public InstructionType getType() {
        return type;
    }

    public String[] getOperands() {
        return operands.clone();
    }

    public int getSize() {
        return size;
    }

    public boolean requiresMemoryAccess() {
        return requiresMemoryAccess;
    }

    public Set<RegisterType> getSourceRegisters() {
        return Collections.unmodifiableSet(sourceRegisters);
    }

    public Set<RegisterType> getDestinationRegisters() {
        return Collections.unmodifiableSet(destinationRegisters);
    }

    public void setMemoryAddress(int address) {
        this.memoryAddress = address;
    }

    public int getMemoryAddress() {
        return memoryAddress;
    }

    public int getCycles() {
        return cycles;
    }

    public boolean hasRegisterDependency(Instruction other) {
        // Check if this instruction's source registers overlap with other's destination
        for (RegisterType reg : sourceRegisters) {
            if (other.destinationRegisters.contains(reg)) {
                return true;
            }
        }

        // Check if this instruction's destination registers overlap with other's source or destination
        for (RegisterType reg : destinationRegisters) {
            if (other.sourceRegisters.contains(reg) || other.destinationRegisters.contains(reg)) {
                return true;
            }
        }

        return false;
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