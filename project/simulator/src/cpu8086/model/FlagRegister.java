package cpu8086.model;

public class FlagRegister {
    private int flags;

    public static final int CARRY_FLAG = 0x0001;
    public static final int PARITY_FLAG = 0x0004;
    public static final int AUXILIARY_FLAG = 0x0010;
    public static final int ZERO_FLAG = 0x0040;
    public static final int SIGN_FLAG = 0x0080;
    public static final int TRAP_FLAG = 0x0100;
    public static final int INTERRUPT_FLAG = 0x0200;
    public static final int DIRECTION_FLAG = 0x0400;
    public static final int OVERFLOW_FLAG = 0x0800;

    public FlagRegister() {
        reset();
    }

    public void reset() {
        flags = 0x0000;
    }

    // Individual flag setters
    public void setCarryFlag(boolean value) {
        setFlag(CARRY_FLAG, value);
    }

    public void setParityFlag(boolean value) {
        setFlag(PARITY_FLAG, value);
    }

    public void setAuxiliaryFlag(boolean value) {
        setFlag(AUXILIARY_FLAG, value);
    }

    public void setZeroFlag(boolean value) {
        setFlag(ZERO_FLAG, value);
    }

    public void setSignFlag(boolean value) {
        setFlag(SIGN_FLAG, value);
    }

    public void setTrapFlag(boolean value) {
        setFlag(TRAP_FLAG, value);
    }

    public void setInterruptFlag(boolean value) {
        setFlag(INTERRUPT_FLAG, value);
    }

    public void setDirectionFlag(boolean value) {
        setFlag(DIRECTION_FLAG, value);
    }

    public void setOverflowFlag(boolean value) {
        setFlag(OVERFLOW_FLAG, value);
    }

    // Individual flag getters
    public boolean isCarryFlag() {
        return getFlag(CARRY_FLAG);
    }

    public boolean isParityFlag() {
        return getFlag(PARITY_FLAG);
    }

    public boolean isAuxiliaryFlag() {
        return getFlag(AUXILIARY_FLAG);
    }

    public boolean isZeroFlag() {
        return getFlag(ZERO_FLAG);
    }

    public boolean isSignFlag() {
        return getFlag(SIGN_FLAG);
    }

    public boolean isTrapFlag() {
        return getFlag(TRAP_FLAG);
    }

    public boolean isInterruptFlag() {
        return getFlag(INTERRUPT_FLAG);
    }

    public boolean isDirectionFlag() {
        return getFlag(DIRECTION_FLAG);
    }

    public boolean isOverflowFlag() {
        return getFlag(OVERFLOW_FLAG);
    }

    // Raw flags access
    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags & 0xFFF; // Only use the lower 12 bits
    }

    // Helper methods
    private void setFlag(int flag, boolean value) {
        if (value) {
            flags |= flag;
        } else {
            flags &= ~flag;
        }
    }

    private boolean getFlag(int flag) {
        return (flags & flag) != 0;
    }
}