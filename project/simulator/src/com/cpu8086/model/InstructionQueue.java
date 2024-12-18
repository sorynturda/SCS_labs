package com.cpu8086.model;

import java.util.LinkedList;
import java.util.Queue;

public class InstructionQueue {
    private Queue<String> instructions;

    public InstructionQueue() {
        this.instructions = new LinkedList<>();
    }

    public void addInstruction(String instruction) {
        instructions.offer(instruction);
    }

    public String getNextInstruction() {
        return instructions.poll();
    }

    public boolean hasInstructions() {
        return !instructions.isEmpty();
    }

    public void clear() {
        instructions.clear();
    }

    public int size() {
        return instructions.size();
    }
}