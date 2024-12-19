package cpu8086.controller;

import com.cpu8086.model.*;
import com.cpu8086.exception.*;
import cpu8086.exception.CPUException;
import cpu8086.exception.InvalidOperandException;
import cpu8086.model.CPU;
import cpu8086.model.Instruction;
import cpu8086.model.Program;
import cpu8086.model.RegisterType;

public class InstructionExecutor {
    private CPU cpu;
    private Program program;
    private int instructionPointer;
    private boolean jumpFlag;

    public InstructionExecutor(CPU cpu, Program program) {
        this.cpu = cpu;
        this.program = program;
        this.instructionPointer = 0;
        this.jumpFlag = false;
    }


    public void execute(Instruction instruction) throws CPUException {
        executeInstruction(instruction);
    }

    private void executeInstruction(Instruction instruction) throws CPUException {
        switch (instruction.getType()) {
            case MOV:
                executeMov(instruction.getOperands());
                break;
            case ADD:
                executeAdd(instruction.getOperands());
                break;
            case SUB:
                executeSub(instruction.getOperands());
                break;
            case OR:
                executeOr(instruction.getOperands());
                break;
            case XOR:
                executeXor(instruction.getOperands());
                break;
            case AND:
                executeAnd(instruction.getOperands());
                break;
            case MUL:
                executeMul(instruction.getOperands());
                break;
            case JMP:
                executeJmp(instruction.getOperands());
                break;
            case CMP:
                executeCmp(instruction.getOperands());
                break;
            case INC:
                executeInc(instruction.getOperands());
                break;
            case DEC:
                executeDec(instruction.getOperands());
                break;
            case SHR:
                executeShr(instruction.getOperands());
                break;
            case JZ:
                executeJz(instruction.getOperands());
                break;
            case LOOP:
                executeLoop(instruction.getOperands());
                break;
            case JG:
                executeJg(instruction.getOperands());
                break;
            default:
                throw new InvalidOperandException("Unsupported instruction: " + instruction.getType());
        }
    }

    private void executeMov(String[] operands) throws CPUException {
        if (operands.length != 2) {
            throw new InvalidOperandException("MOV requires two operands");
        }

        String dest = operands[0].trim();
        String source = operands[1].trim();

        // If source is an immediate value and destination is memory
        if (dest.startsWith("[") && !source.startsWith("[")) {
            try {
                int value = parseOperand(source);
                setOperandValue(dest, value);
                return;
            } catch (Exception e) {
                // Not an immediate value, continue with normal processing
            }
        }

        int value = getOperandValue(source);
        setOperandValue(dest, value);
    }

    private void executeAdd(String[] operands) throws CPUException {
        if (operands.length != 2) {
            throw new InvalidOperandException("ADD requires two operands");
        }

        String dest = operands[0].trim();
        int value1 = getOperandValue(dest);
        int value2 = getOperandValue(operands[1].trim());

        int result = cpu.getALU().add(value1, value2);
        setOperandValue(dest, result);
    }

    private void executeSub(String[] operands) throws CPUException {
        if (operands.length != 2) {
            throw new InvalidOperandException("SUB requires two operands");
        }

        String dest = operands[0].trim();
        int value1 = getOperandValue(dest);
        int value2 = getOperandValue(operands[1].trim());

        int result = cpu.getALU().subtract(value1, value2);
        setOperandValue(dest, result);
    }

    private void executeOr(String[] operands) throws CPUException {
        if (operands.length != 2) {
            throw new InvalidOperandException("OR requires two operands");
        }

        String dest = operands[0].trim();
        int value1 = getOperandValue(dest);
        int value2 = getOperandValue(operands[1].trim());

        setOperandValue(dest, value1 | value2);

        // Update flags
        cpu.getFlags().setZeroFlag((value1 | value2) == 0);
        cpu.getFlags().setSignFlag(((value1 | value2) & 0x8000) != 0);
        cpu.getFlags().setCarryFlag(false);
        cpu.getFlags().setOverflowFlag(false);
    }

    private void executeXor(String[] operands) throws CPUException {
        if (operands.length != 2) {
            throw new InvalidOperandException("XOR requires two operands");
        }

        String dest = operands[0].trim();
        int value1 = getOperandValue(dest);
        int value2 = getOperandValue(operands[1].trim());

        setOperandValue(dest, value1 ^ value2);

        // Update flags
        cpu.getFlags().setZeroFlag((value1 ^ value2) == 0);
        cpu.getFlags().setSignFlag(((value1 ^ value2) & 0x8000) != 0);
        cpu.getFlags().setCarryFlag(false);
        cpu.getFlags().setOverflowFlag(false);
    }

    private void executeAnd(String[] operands) throws CPUException {
        if (operands.length != 2) {
            throw new InvalidOperandException("AND requires two operands");
        }

        String dest = operands[0].trim();
        int value1 = getOperandValue(dest);
        int value2 = getOperandValue(operands[1].trim());

        setOperandValue(dest, value1 & value2);

        // Update flags
        cpu.getFlags().setZeroFlag((value1 & value2) == 0);
        cpu.getFlags().setSignFlag(((value1 & value2) & 0x8000) != 0);
        cpu.getFlags().setCarryFlag(false);
        cpu.getFlags().setOverflowFlag(false);
    }

    private void executeMul(String[] operands) throws CPUException {
        if (operands.length != 1) {
            throw new InvalidOperandException("MUL requires one operand");
        }
        int value = parseOperand(operands[0]);
        int ax = cpu.getRegisters().getRegister(RegisterType.AX);
        long result = cpu.getALU().multiply(ax, value);
        cpu.getRegisters().setRegister(RegisterType.AX, (int) (result & 0xFFFF));
        cpu.getRegisters().setRegister(RegisterType.DX, (int) (result >> 16));
    }

    private void executeJmp(String[] operands) throws CPUException {
        if (operands.length != 1) {
            throw new InvalidOperandException("JMP requires one operand");
        }

        try {
            // Parse target line number
            int targetLine = Integer.parseInt(operands[0].trim());

            // Verificăm dacă linia țintă este validă
            if (targetLine <= 0 || targetLine > program.getInstructionCount()) {
                throw new InvalidOperandException("Invalid jump target line: " + targetLine);
            }

            program.jumpToInstruction(targetLine);
            jumpFlag = true;

            // Actualizăm și IP-ul pentru a reflecta noua poziție
            cpu.getRegisters().setRegister(RegisterType.IP, targetLine - 1);

        } catch (NumberFormatException e) {
            throw new InvalidOperandException("Invalid jump target format: " + operands[0]);
        }
    }


    private void executeCmp(String[] operands) throws CPUException {
        if (operands.length != 2) {
            throw new InvalidOperandException("CMP requires two operands");
        }

        int value1 = getOperandValue(operands[0].trim());
        int value2 = getOperandValue(operands[1].trim());

        // Perform signed comparison
        int result = value1 - value2;

        // Set Zero Flag (ZF)
        cpu.getFlags().setZeroFlag(result == 0);

        // Set Sign Flag (SF)
        cpu.getFlags().setSignFlag((result & 0x8000) != 0);

        // Set Carry Flag (CF) - unsigned comparison
        cpu.getFlags().setCarryFlag((value1 & 0xFFFF) < (value2 & 0xFFFF));

        // Set Overflow Flag (OF) - signed comparison
        boolean overflow = (((value1 ^ value2) & (value1 ^ result) & 0x8000) != 0);
        cpu.getFlags().setOverflowFlag(overflow);
    }


    private void executeInc(String[] operands) throws CPUException {
        if (operands.length != 1) {
            throw new InvalidOperandException("INC requires one operand");
        }

        String dest = operands[0].trim();
        int value = getOperandValue(dest);
        setOperandValue(dest, value + 1);

        // Update flags
        cpu.getFlags().setZeroFlag((value + 1) == 0);
        cpu.getFlags().setSignFlag(((value + 1) & 0x8000) != 0);
        cpu.getFlags().setOverflowFlag(value == 0x7FFF);
    }

    private void executeDec(String[] operands) throws CPUException {
        if (operands.length != 1) {
            throw new InvalidOperandException("DEC requires one operand");
        }

        String dest = operands[0].trim();
        int value = getOperandValue(dest);
        setOperandValue(dest, value - 1);

        // Update flags
        cpu.getFlags().setZeroFlag((value - 1) == 0);
        cpu.getFlags().setSignFlag(((value - 1) & 0x8000) != 0);
        cpu.getFlags().setOverflowFlag(value == 0x8000);
    }

    private void executeShr(String[] operands) throws CPUException {
        if (operands.length != 2) {
            throw new InvalidOperandException("SHR requires two operands");
        }
        int value = getOperandValue(operands[0]);
        int count = parseOperand(operands[1]);
        setOperandValue(operands[0], value >> count);
    }

    private void executeJz(String[] operands) throws CPUException {
        if (operands.length != 1) {
            throw new InvalidOperandException("JZ requires one operand");
        }

        try {
            // Parse target line number
            int targetLine = Integer.parseInt(operands[0].trim());

            // Check if zero flag is set
            if (cpu.getFlags().isZeroFlag()) {
                // Validate target line
                if (targetLine <= 0 || targetLine > program.getInstructionCount()) {
                    throw new InvalidOperandException("Invalid jump target line: " + targetLine);
                }

                // Perform the jump
                program.jumpToInstruction(targetLine);
                jumpFlag = true;

                // Update IP register
                cpu.getRegisters().setRegister(RegisterType.IP, targetLine - 1);
            }
        } catch (NumberFormatException e) {
            throw new InvalidOperandException("Invalid jump target format: " + operands[0]);
        }
    }

    private void executeLoop(String[] operands) throws CPUException {
        if (operands.length != 1) {
            throw new InvalidOperandException("LOOP requires one operand");
        }
        int cx = cpu.getRegisters().getRegister(RegisterType.CX);
        cx--;
        cpu.getRegisters().setRegister(RegisterType.CX, cx);
        if (cx != 0) {
            int target = parseOperand(operands[0]);
            instructionPointer = target;
            jumpFlag = true;
        }
    }

    private void executeJg(String[] operands) throws CPUException {
        if (operands.length != 1) {
            throw new InvalidOperandException("JG requires one operand");
        }

        try {
            int targetLine = Integer.parseInt(operands[0].trim());

            // JG (Jump if Greater) - ZF=0 AND SF=OF
            boolean shouldJump = !cpu.getFlags().isZeroFlag() &&
                    (cpu.getFlags().isSignFlag() == cpu.getFlags().isOverflowFlag());

            if (shouldJump) {
                // Validate jump target
                if (targetLine <= 0 || targetLine > program.getInstructionCount()) {
                    throw new InvalidOperandException("Invalid jump target line: " + targetLine);
                }

                // Perform the jump
                program.jumpToInstruction(targetLine);
                jumpFlag = true;
                cpu.getRegisters().setRegister(RegisterType.IP, targetLine - 1);
            }
        } catch (NumberFormatException e) {
            throw new InvalidOperandException("Invalid jump target format: " + operands[0]);
        }
    }

    private int parseOperand(String operand) throws CPUException {
        operand = operand.trim();

        // Check if it's a memory reference
        if (operand.startsWith("[") && operand.endsWith("]")) {
            String addressStr = operand.substring(1, operand.length() - 1);
            return parseAddress(addressStr);
        }

        // Handle negative numbers
        boolean isNegative = operand.startsWith("-");
        if (isNegative) {
            operand = operand.substring(1);
        }

        // Parse the number
        try {
            int value;
            if (operand.endsWith("H") || operand.endsWith("h")) {
                // Hexadecimal
                value = Integer.parseInt(operand.substring(0, operand.length() - 1), 16);
            } else if (operand.endsWith("B") || operand.endsWith("b")) {
                // Binary
                value = Integer.parseInt(operand.substring(0, operand.length() - 1), 2);
            } else {
                // Try register first
                try {
                    RegisterType reg = RegisterType.valueOf(operand.toUpperCase());
                    return cpu.getRegisters().getRegister(reg);
                } catch (IllegalArgumentException ex) {
                    // Not a register, treat as decimal
                    value = Integer.parseInt(operand);
                }
            }

            // Apply sign if negative
            if (isNegative) {
                value = -value;
            }

            // Check if the value is within the signed 16-bit range
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                throw new InvalidOperandException("Operand value out of range: " + value);
            }

            return value;
        } catch (NumberFormatException e) {
            throw new InvalidOperandException("Invalid operand format: " + operand);
        }
    }

    private int parseAddress(String addressStr) throws CPUException {
        try {
            boolean isNegative = addressStr.startsWith("-");
            if (isNegative) {
                addressStr = addressStr.substring(1);
            }

            int address;
            if (addressStr.endsWith("H") || addressStr.endsWith("h")) {
                address = Integer.parseInt(addressStr.substring(0, addressStr.length() - 1), 16);
            } else {
                address = Integer.parseInt(addressStr);
            }

            if (isNegative) {
                address = -address;
            }

            if (address < 0 || address > 0xFFFFF) {
                throw new InvalidOperandException("Address out of range: " + addressStr);
            }

            return address;
        } catch (NumberFormatException e) {
            throw new InvalidOperandException("Invalid address format: " + addressStr);
        }
    }

    private int getOperandValue(String operand) throws CPUException {
        operand = operand.trim().toUpperCase();

        // Check if it's a memory reference
        if (operand.startsWith("[") && operand.endsWith("]")) {
            String addressStr = operand.substring(1, operand.length() - 1);

            // Remove H suffix if present
            if (addressStr.endsWith("H")) {
                addressStr = addressStr.substring(0, addressStr.length() - 1);
            }

            try {
                // Parse hexadecimal address
                int address = Integer.parseInt(addressStr, 16);
                // Read word from memory
                return cpu.getMemory().readWord(address);
            } catch (NumberFormatException e) {
                throw new InvalidOperandException("Invalid memory address: " + addressStr);
            }
        }

        // Try register or immediate value
        return parseOperand(operand);
    }

    private void setOperandValue(String operand, int value) throws CPUException {
        operand = operand.trim().toUpperCase();

        // Check if it's a memory reference
        if (operand.startsWith("[") && operand.endsWith("]")) {
            String addressStr = operand.substring(1, operand.length() - 1);

            // Remove H suffix if present
            if (addressStr.endsWith("H")) {
                addressStr = addressStr.substring(0, addressStr.length() - 1);
            }

            try {
                // Parse hexadecimal address
                int address = Integer.parseInt(addressStr, 16);
                // Write word to memory
                cpu.getMemory().writeWord(address, value);
                return;
            } catch (NumberFormatException e) {
                throw new InvalidOperandException("Invalid memory address: " + addressStr);
            }
        }

        // If not memory, must be a register
        try {
            RegisterType reg = RegisterType.valueOf(operand);
            cpu.getRegisters().setRegister(reg, value);
        } catch (IllegalArgumentException e) {
            throw new InvalidOperandException("Invalid operand: " + operand);
        }
    }

    public boolean isJumpFlag() {
        return jumpFlag;
    }

    public void clearJumpFlag() {
        jumpFlag = false;
    }

    public int getInstructionPointer() {
        return instructionPointer;
    }

    public void setInstructionPointer(int ip) {
        instructionPointer = ip;
    }
}