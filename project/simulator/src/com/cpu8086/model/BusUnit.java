package com.cpu8086.model;

/**
 * Represents the Bus Interface Unit (BIU) of the 8086 processor.
 * Responsible for:
 * - Fetching instructions from memory
 * - Managing the instruction queue
 * - Generating physical addresses
 * - Handling bus control signals
 */
public class BusUnit {
    private static final int QUEUE_SIZE = 6;
    private byte[] instructionQueue;
    private int queueSize;
    private int queuePosition;

    // Segment registers are used for address calculation
    private int codeSegment;
    private int dataSegment;
    private int stackSegment;
    private int extraSegment;

    private boolean busLocked;
    private boolean requestPending;

    public BusUnit() {
        instructionQueue = new byte[QUEUE_SIZE];
        reset();
    }

    /**
     * Resets the bus unit to its initial state
     */
    public void reset() {
        queueSize = 0;
        queuePosition = 0;
        codeSegment = 0;
        dataSegment = 0;
        stackSegment = 0;
        extraSegment = 0;
        busLocked = false;
        requestPending = false;
        clearQueue();
    }

    /**
     * Clears the instruction queue
     */
    public void clearQueue() {
        for (int i = 0; i < QUEUE_SIZE; i++) {
            instructionQueue[i] = 0;
        }
        queueSize = 0;
        queuePosition = 0;
    }

    /**
     * Calculates physical address from segment and offset
     */
    public int calculatePhysicalAddress(int segment, int offset) {
        return (segment << 4) + offset;
    }

    /**
     * Adds a byte to the instruction queue
     */
    public void queueInstruction(byte instruction) {
        if (queueSize < QUEUE_SIZE) {
            instructionQueue[(queuePosition + queueSize) % QUEUE_SIZE] = instruction;
            queueSize++;
        }
    }

    /**
     * Gets next byte from the instruction queue
     */
    public byte getNextByte() {
        if (queueSize > 0) {
            byte value = instructionQueue[queuePosition];
            queuePosition = (queuePosition + 1) % QUEUE_SIZE;
            queueSize--;
            return value;
        }
        return 0;
    }

    /**
     * Checks if the queue has enough bytes available
     */
    public boolean hasBytes(int count) {
        return queueSize >= count;
    }

    /**
     * Sets the bus lock status
     */
    public void setBusLock(boolean locked) {
        this.busLocked = locked;
    }

    /**
     * Checks if the bus is currently locked
     */
    public boolean isBusLocked() {
        return busLocked;
    }

    /**
     * Sets a pending bus request
     */
    public void setRequestPending(boolean pending) {
        this.requestPending = pending;
    }

    /**
     * Checks if there's a pending bus request
     */
    public boolean isRequestPending() {
        return requestPending;
    }

    // Segment register getters and setters
    public int getCodeSegment() {
        return codeSegment;
    }

    public void setCodeSegment(int value) {
        codeSegment = value & 0xFFFF;
    }

    public int getDataSegment() {
        return dataSegment;
    }

    public void setDataSegment(int value) {
        dataSegment = value & 0xFFFF;
    }

    public int getStackSegment() {
        return stackSegment;
    }

    public void setStackSegment(int value) {
        stackSegment = value & 0xFFFF;
    }

    public int getExtraSegment() {
        return extraSegment;
    }

    public void setExtraSegment(int value) {
        extraSegment = value & 0xFFFF;
    }

    /**
     * Gets the current instruction queue size
     */
    public int getQueueSize() {
        return queueSize;
    }
}