package cpu8086.controller;

import cpu8086.exception.CPUException;
import cpu8086.model.CPU;
import cpu8086.model.Instruction;
import cpu8086.model.Program;

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
        this.executor = new InstructionExecutor(cpu, program);
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

    public String getCurrentInstruction() {
        return program.getCurrentInstruction();
    }

    public boolean hasMoreInstructions() {
        return program.hasMoreInstructions();
    }

    public CPU getCPU() {
        return cpu;
    }

    public void reset() {
        cpu.reset();
        program.reset();
    }


    public InstructionDecoder getDecoder() {
        return decoder;
    }
}