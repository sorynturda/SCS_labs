package cpu8086.model;

public enum RegisterType {
    // General purpose registers
    AX, BX, CX, DX,
    // Instruction pointer
    IP,
    // Index registers
    SI, DI,
    // Pointer registers
    BP, SP,
    // Segment registers
    CS, DS, SS, ES
}

