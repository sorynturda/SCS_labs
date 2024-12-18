package com.cpu8086.model;


public class Instruction {
    private InstructionType type;
    private String[] operands;

    public Instruction(com.cpu8086.model.InstructionType type, String[] operands) {
        this.type = type;
        this.operands = operands;
    }

    public com.cpu8086.model.InstructionType getType() {
        return type;
    }

    public String[] getOperands() {
        return operands;
    }
}

