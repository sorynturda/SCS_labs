package com.cpu8086.controller;

import com.cpu8086.model.*;
import com.cpu8086.exception.*;

public class CPUController {
    private CPU cpu;
    private InstructionDecoder decoder;
    private InstructionExecutor executor;
    private InstructionQueue instructionQueue;

    public CPUController() {
        this.cpu = new CPU();
        this.decoder = new InstructionDecoder();
        this.executor = new InstructionExecutor(cpu);
        this.instructionQueue = new InstructionQueue();
    }

    public void queueInstruction(String instruction) {
        instructionQueue.addInstruction(instruction);
    }

    public void executeNext() throws CPUException {
        if (instructionQueue.hasInstructions()) {
            String instruction = instructionQueue.getNextInstruction();
            Instruction decodedInstr = decoder.decode(instruction);
            executor.execute(decodedInstr);
        }
    }

    public void executeAll() throws CPUException {
        while (instructionQueue.hasInstructions()) {
            executeNext();
        }
    }

    public void reset() {
        cpu.reset();
        instructionQueue.clear();
    }

    public CPU getCPU() {
        return cpu;
    }
}