package com.cpu8086.controller;

import com.cpu8086.exception.InvalidOperandException;
import com.cpu8086.model.Instruction;
import com.cpu8086.model.InstructionType;

public class InstructionDecoder {
    public Instruction decode(String instructionText) throws InvalidOperandException {
        String[] parts = instructionText.trim().toUpperCase().split(" ", 2);

        if (parts.length < 1) {
            throw new InvalidOperandException("Empty instruction");
        }

        InstructionType type = InstructionType.valueOf(parts[0]);
        String[] operands = parts.length > 1 ? parts[1].split(",") : new String[0];

        return new Instruction(type, operands);
    }
}

