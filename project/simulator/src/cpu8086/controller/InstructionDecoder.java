package cpu8086.controller;

import cpu8086.exception.InvalidOperandException;
import cpu8086.model.Instruction;
import cpu8086.model.InstructionType;

public class InstructionDecoder {
    public Instruction decode(String instructionText) throws InvalidOperandException {
        try {
            String[] parts = instructionText.trim().toUpperCase().split(" ", 2);

            if (parts.length < 1 || parts[0].isEmpty()) {
                throw new InvalidOperandException("Empty instruction");
            }

            // Check if instruction exists
            try {
                InstructionType type = InstructionType.valueOf(parts[0]);
                String[] operands = parts.length > 1 ? parts[1].split(",") : new String[0];

                // Strip whitespace from operands
                for (int i = 0; i < operands.length; i++) {
                    operands[i] = operands[i].trim();
                }

                return new Instruction(type, operands);
            } catch (IllegalArgumentException e) {
                throw new InvalidOperandException("Unknown instruction: " + parts[0]);
            }
        } catch (Exception e) {
            if (e instanceof InvalidOperandException) {
                throw (InvalidOperandException) e;
            }
            throw new InvalidOperandException("Invalid instruction format: " + instructionText);
        }
    }

}

