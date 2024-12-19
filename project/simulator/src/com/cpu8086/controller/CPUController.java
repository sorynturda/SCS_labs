package com.cpu8086.controller;

import com.cpu8086.exception.CPUException;
import com.cpu8086.model.CPU;
import com.cpu8086.model.Instruction;
import com.cpu8086.model.Program;

public class CPUController {
    private CPU cpu;
    private Program program;
    private InstructionDecoder decoder;
    private InstructionExecutor executor;
    private boolean stepMode;

    public CPUController() {
        this.cpu = new CPU();
        this.program = new Program();
        this.decoder = new InstructionDecoder();
        this.executor = new InstructionExecutor(cpu);
        this.stepMode = true;
    }

    public void loadProgram(String programText) {
        program.loadProgram(programText);
        cpu.reset();
    }

    public void executeNextStep() throws CPUException {
        if (program.hasMoreInstructions()) {
            String instruction = program.getCurrentInstruction();
            Instruction decodedInstr = decoder.decode(instruction);
            executor.execute(decodedInstr);
            program.nextInstruction();
        }
    }

    public void executeAll() throws CPUException {
        while (program.hasMoreInstructions()) {
            executeNextStep();
        }
    }

    public boolean hasMoreInstructions() {
        return program.hasMoreInstructions();
    }

    public int getCurrentLine() {
        return program.getCurrentLine();
    }

    public CPU getCPU() {
        return cpu;
    }

    public void reset() {
        cpu.reset();
        program.reset();
    }

    public void setStepMode(boolean stepMode) {
        this.stepMode = stepMode;
    }

    public boolean isStepMode() {
        return stepMode;
    }
}