package com.cpu8086.model;

import java.util.Arrays;

public class Memory {
    private static final int MEMORY_SIZE = 1048576; // 1MB = 1024 * 1024 bytes
    private byte[] memory;

    public Memory() {
        memory = new byte[MEMORY_SIZE];
        reset();
    }

    public void reset() {
        Arrays.fill(memory, (byte) 0);
    }

    public byte readByte(int physicalAddress) {
        validateAddress(physicalAddress);
        return memory[physicalAddress];
    }

    public void writeByte(int physicalAddress, byte value) {
        validateAddress(physicalAddress);
        memory[physicalAddress] = value;
    }

    public int readWord(int physicalAddress) {
        validateAddress(physicalAddress);
        validateAddress(physicalAddress + 1);
        return (memory[physicalAddress] & 0xFF) | ((memory[physicalAddress + 1] & 0xFF) << 8);
    }

    public void writeWord(int physicalAddress, int value) {
        validateAddress(physicalAddress);
        validateAddress(physicalAddress + 1);
        memory[physicalAddress] = (byte)(value & 0xFF);
        memory[physicalAddress + 1] = (byte)((value >> 8) & 0xFF);
    }

    private void validateAddress(int address) {
        if (address < 0 || address >= MEMORY_SIZE) {
            throw new IllegalArgumentException(
                String.format("Physical address %05XH is out of range (00000H-%05XH)",
                    address, MEMORY_SIZE - 1)
            );
        }
    }

    // Utility method to dump memory contents for debugging
    public String dumpMemory(int startAddress, int length) {
        validateAddress(startAddress);
        validateAddress(startAddress + length - 1);

        StringBuilder sb = new StringBuilder();

        // Print header
        sb.append("       +0 +1 +2 +3 +4 +5 +6 +7 +8 +9 +A +B +C +D +E +F\n");
        sb.append("-------------------------------------------------------\n");

        // Print memory contents
        for (int base = startAddress & 0xFFFFF0; base < startAddress + length; base += 16) {
            sb.append(String.format("%05X: ", base));

            // Print hex values
            for (int offset = 0; offset < 16; offset++) {
                int addr = base + offset;
                if (addr >= startAddress && addr < startAddress + length) {
                    sb.append(String.format("%02X ", memory[addr] & 0xFF));
                } else {
                    sb.append("   ");
                }
            }

            sb.append(" | ");

            // Print ASCII representation
            for (int offset = 0; offset < 16; offset++) {
                int addr = base + offset;
                if (addr >= startAddress && addr < startAddress + length) {
                    char c = (char)(memory[addr] & 0xFF);
                    sb.append(isPrintable(c) ? c : '.');
                } else {
                    sb.append(" ");
                }
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private boolean isPrintable(char c) {
        return c >= 32 && c < 127;
    }

    // Get total memory size
    public int getSize() {
        return MEMORY_SIZE;
    }
}