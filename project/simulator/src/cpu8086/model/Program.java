package cpu8086.model;

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

        String[] lines = program.split("\n");
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty() && !trimmedLine.startsWith(";")) {
                instructions.add(trimmedLine);
            }
        }
    }

    public String getCurrentInstruction() {
        return currentLine < instructions.size() ? instructions.get(currentLine) : null;
    }

    public void jumpToInstruction(int targetLine) {
        currentLine = targetLine - 1;
        if (currentLine < 0 || currentLine >= instructions.size()) {
            throw new IllegalArgumentException("Invalid jump target: " + targetLine);
        }
    }

    public boolean hasMoreInstructions() {
        return currentLine < instructions.size();
    }

    public void nextInstruction() {
        if (hasMoreInstructions()) {
            currentLine++;
        }
    }


    public void reset() {
        currentLine = 0;
        running = false;
    }

    public List<String> getInstructions() {
        return instructions;
    }


    public int getInstructionCount() {
        return instructions.size();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}