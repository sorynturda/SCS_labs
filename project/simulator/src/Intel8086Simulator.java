import java.util.HashMap;
import java.util.Map;

public class Intel8086Simulator {
    private Map<String, Integer> registers;
    private int[] memory;
    private boolean zeroFlag;
    private boolean signFlag;
    private boolean carryFlag;
    private boolean overflowFlag;
    private int ip;

    public Intel8086Simulator() {
        registers = new HashMap<>();
        registers.put("AX", 0);
        registers.put("BX", 0);
        registers.put("CX", 0);
        registers.put("DX", 0);
        registers.put("SI", 0);
        registers.put("DI", 0);
        registers.put("SP", 0);
        registers.put("BP", 0);

        memory = new int[65536];
        resetFlags();
        ip = 0;
    }

    private void resetFlags() {
        zeroFlag = false;
        signFlag = false;
        carryFlag = false;
        overflowFlag = false;
    }

    private int parseOperand(String operand) {
        operand = operand.trim();

        if (operand.startsWith("[") && operand.endsWith("]")) {
            String memRef = operand.substring(1, operand.length() - 1).trim();
            return memory[getMemoryAddress(memRef)];
        }

        if (registers.containsKey(operand)) {
            return registers.get(operand);
        }

        try {
            if (operand.startsWith("0X") || operand.startsWith("0x")) {
                return Integer.parseInt(operand.substring(2), 16);
            }
            return Integer.parseInt(operand);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid operand: " + operand);
        }
    }

    private int getMemoryAddress(String memRef) {
        if (registers.containsKey(memRef)) {
            return registers.get(memRef) & 0xFFFF;
        }
        try {
            if (memRef.startsWith("0X") || memRef.startsWith("0x")) {
                return Integer.parseInt(memRef.substring(2), 16);
            }
            return Integer.parseInt(memRef) & 0xFFFF;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid memory reference: " + memRef);
        }
    }

    private void writeToMemory(String dest, int value) {
        if (dest.startsWith("[") && dest.endsWith("]")) {
            String memRef = dest.substring(1, dest.length() - 1).trim();
            int address = getMemoryAddress(memRef);
            memory[address] = value & 0xFF;
        } else if (registers.containsKey(dest)) {
            registers.put(dest, value & 0xFFFF);
        } else {
            throw new IllegalArgumentException("Invalid destination: " + dest);
        }
    }

    private void updateFlags(int result) {
        zeroFlag = (result & 0xFFFF) == 0;
        signFlag = (result & 0x8000) != 0;
        carryFlag = (result & 0x10000) != 0;
        overflowFlag = (result < -32768 || result > 32767);
    }

    // Instructions
    public void mov(String dest, String src) {
        int value = parseOperand(src);
        writeToMemory(dest, value);
    }

    public void add(String dest, String src) {
        int destValue;
        int srcValue = parseOperand(src);

        if (dest.startsWith("[")) {
            int address = getMemoryAddress(dest.substring(1, dest.length() - 1));
            destValue = memory[address];
            int result = destValue + srcValue;
            updateFlags(result);
            memory[address] = result & 0xFF;
        } else {
            destValue = registers.get(dest);
            int result = destValue + srcValue;
            updateFlags(result);
            registers.put(dest, result & 0xFFFF);
        }
    }

    public void sub(String dest, String src) {
        int srcValue = parseOperand(src);
        if (dest.startsWith("[")) {
            int address = getMemoryAddress(dest.substring(1, dest.length() - 1));
            int result = memory[address] - srcValue;
            updateFlags(result);
            memory[address] = result & 0xFF;
        } else {
            int result = registers.get(dest) - srcValue;
            updateFlags(result);
            registers.put(dest, result & 0xFFFF);
        }
    }

    public void and(String dest, String src) {
        int srcValue = parseOperand(src);
        if (dest.startsWith("[")) {
            int address = getMemoryAddress(dest.substring(1, dest.length() - 1));
            int result = memory[address] & srcValue;
            updateFlags(result);
            memory[address] = result & 0xFF;
        } else {
            int result = registers.get(dest) & srcValue;
            updateFlags(result);
            registers.put(dest, result & 0xFFFF);
        }
    }

    public void or(String dest, String src) {
        int srcValue = parseOperand(src);
        if (dest.startsWith("[")) {
            int address = getMemoryAddress(dest.substring(1, dest.length() - 1));
            int result = memory[address] | srcValue;
            updateFlags(result);
            memory[address] = result & 0xFF;
        } else {
            int result = registers.get(dest) | srcValue;
            updateFlags(result);
            registers.put(dest, result & 0xFFFF);
        }
    }

    public void xor(String dest, String src) {
        int srcValue = parseOperand(src);
        if (dest.startsWith("[")) {
            int address = getMemoryAddress(dest.substring(1, dest.length() - 1));
            int result = memory[address] ^ srcValue;
            updateFlags(result);
            memory[address] = result & 0xFF;
        } else {
            int result = registers.get(dest) ^ srcValue;
            updateFlags(result);
            registers.put(dest, result & 0xFFFF);
        }
    }

    public void mul(String src) {
        int srcValue = parseOperand(src);
        int ax = registers.get("AX");
        int result = ax * srcValue;
        registers.put("AX", result & 0xFFFF);
        registers.put("DX", (result >> 16) & 0xFFFF);
        updateFlags(result);
    }

    public void inc(String dest) {
        if (dest.startsWith("[")) {
            int address = getMemoryAddress(dest.substring(1, dest.length() - 1));
            int result = memory[address] + 1;
            updateFlags(result);
            memory[address] = result & 0xFF;
        } else {
            int result = registers.get(dest) + 1;
            updateFlags(result);
            registers.put(dest, result & 0xFFFF);
        }
    }

    public void dec(String dest) {
        if (dest.startsWith("[")) {
            int address = getMemoryAddress(dest.substring(1, dest.length() - 1));
            int result = memory[address] - 1;
            updateFlags(result);
            memory[address] = result & 0xFF;
        } else {
            int result = registers.get(dest) - 1;
            updateFlags(result);
            registers.put(dest, result & 0xFFFF);
        }
    }

    public void shr(String dest, String count) {
        int shiftCount = parseOperand(count);
        if (dest.startsWith("[")) {
            int address = getMemoryAddress(dest.substring(1, dest.length() - 1));
            int result = memory[address] >> shiftCount;
            updateFlags(result);
            memory[address] = result & 0xFF;
        } else {
            int result = registers.get(dest) >> shiftCount;
            updateFlags(result);
            registers.put(dest, result & 0xFFFF);
        }
    }

    public void cmp(String operand1, String operand2) {
        int val1 = parseOperand(operand1);
        int val2 = parseOperand(operand2);
        int result = val1 - val2;
        updateFlags(result);
    }

    public void jmp(int address) {
        ip = address & 0xFFFF;
    }

    public void jz(int address) {
        if (zeroFlag) {
            ip = address & 0xFFFF;
        }
    }

    public void jg(int address) {
        if (!zeroFlag && (signFlag == overflowFlag)) {
            ip = address & 0xFFFF;
        }
    }

    public void shl(String dest, String count) {
        int shiftCount = parseOperand(count);
        if (dest.startsWith("[")) {
            int address = getMemoryAddress(dest.substring(1, dest.length() - 1));
            int result = memory[address] << shiftCount;
            updateFlags(result);
            memory[address] = result & 0xFF;
        } else {
            int result = registers.get(dest) << shiftCount;
            updateFlags(result);
            registers.put(dest, result & 0xFFFF);
        }
    }

    // Getters for GUI
    public Map<String, Integer> getRegisters() {
        return new HashMap<>(registers);
    }

    public int[] getMemory() {
        return memory.clone();
    }

    public Map<String, Boolean> getFlags() {
        Map<String, Boolean> flags = new HashMap<>();
        flags.put("ZF", zeroFlag);
        flags.put("SF", signFlag);
        flags.put("CF", carryFlag);
        flags.put("OF", overflowFlag);
        return flags;
    }

    public int getIP() {
        return ip;
    }
}