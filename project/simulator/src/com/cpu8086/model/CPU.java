package com.cpu8086.model;

public class CPU {
    private RegisterBank registers;
    private Memory memory;
    private ALU alu;
    private FlagRegister flags;
    private BusUnit busUnit;

    public CPU() {
        this.registers = new RegisterBank();
        this.memory = new Memory();
        this.flags = new FlagRegister();
        this.alu = new ALU(this.flags);  // Pass the shared flags
        this.busUnit = new BusUnit();
    }

    public void reset() {
        registers.reset();
        flags.reset();
        busUnit.reset();
    }

    public RegisterBank getRegisters() {
        return registers;
    }

    public Memory getMemory() {
        return memory;
    }

    public ALU getALU() {
        return alu;
    }

    public FlagRegister getFlags() {
        return flags;
    }

    public BusUnit getBusUnit() {
        return busUnit;
    }
}
