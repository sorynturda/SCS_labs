package com.cpu8086.model;

import java.util.ArrayList;
import java.util.List;

public class Program {
    private List<String> instructions;
    private int currentLine;
    private boolean running;

    public Program() {
        this.instructions = new ArrayList<>();
        this.currentLine = 0;
        this.running = false;
    }

    public void loadProgram(String program) {
        instructions.clear();
        currentLine = 0;
        running = false;

        // Split program into lines and add valid instructions
        String[] lines = program.split("\n");
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty() && !trimmedLine.startsWith(";")) {
                instructions.add(trimmedLine);
            }
        }
    }

    public String getCurrentInstruction() {
        if (currentLine < instructions.size()) {
            return instructions.get(currentLine);
        }
        return null;
    }

    public boolean hasMoreInstructions() {
        return currentLine < instructions.size();
    }

    public void nextInstruction() {
        if (hasMoreInstructions()) {
            currentLine++;
        }
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void reset() {
        currentLine = 0;
        running = false;
    }

    public List<String> getInstructions() {
        return new ArrayList<>(instructions);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
