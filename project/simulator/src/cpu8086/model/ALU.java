package cpu8086.model;

public class ALU {
    private static final int MAX_16BIT = 0x7FFF;  // Maximum positive 16-bit signed value
    private static final int MIN_16BIT = 0x8000;  // Minimum negative 16-bit signed value
    private FlagRegister flags;

    public ALU(FlagRegister flags) {
        this.flags = flags;
    }

    public int add(int a, int b) {
        // Convert to signed 16-bit values
        a = toSigned16Bit(a);
        b = toSigned16Bit(b);

        int result = a + b;

        // Check for overflow in signed addition
        boolean overflow = false;
        if (a > 0 && b > 0 && result <= 0) {
            overflow = true;  // Positive + Positive = Negative
        } else if (a < 0 && b < 0 && result >= 0) {
            overflow = true;  // Negative + Negative = Positive
        }

        updateArithmeticFlags(result, a, b, false, overflow);
        return result & 0xFFFF;
    }

    public int subtract(int a, int b) {
        // Convert to signed 16-bit values
        a = toSigned16Bit(a);
        b = toSigned16Bit(b);

        int result = a - b;

        // Check for overflow in signed subtraction
        boolean overflow = false;
        if (a >= 0 && b < 0 && result < 0) {
            overflow = true;  // Positive - Negative = Negative
        } else if (a < 0 && b >= 0 && result > 0) {
            overflow = true;  // Negative - Positive = Positive
        }

        updateArithmeticFlags(result, a, b, true, overflow);
        return result & 0xFFFF;
    }

    public long multiply(int a, int b) {
        // Convert to signed 16-bit values
        a = toSigned16Bit(a);
        b = toSigned16Bit(b);

        long result = (long)a * (long)b;

        // Check for signed multiplication overflow
        boolean overflow = (result > MAX_16BIT || result < MIN_16BIT);

        flags.setCarryFlag(overflow);
        flags.setOverflowFlag(overflow);
        flags.setZeroFlag((result & 0xFFFF) == 0);
        flags.setSignFlag((result & 0x8000) != 0);
        flags.setParityFlag(getParity((int)(result & 0xFF)));

        return result & 0xFFFF;
    }

    private void updateArithmeticFlags(int result, int a, int b, boolean isSubtract, boolean overflow) {
        // Carry flag - for unsigned arithmetic
        if (isSubtract) {
            flags.setCarryFlag((a & 0xFFFF) < (b & 0xFFFF));
        } else {
            flags.setCarryFlag((result & 0x10000) != 0);
        }

        // Zero flag
        flags.setZeroFlag((result & 0xFFFF) == 0);

        // Sign flag - MSB indicates sign in signed arithmetic
        flags.setSignFlag((result & 0x8000) != 0);

        // Overflow flag - for signed arithmetic
        flags.setOverflowFlag(overflow);

        // Auxiliary flag
        if (isSubtract) {
            flags.setAuxiliaryFlag((a & 0xF) < (b & 0xF));
        } else {
            flags.setAuxiliaryFlag(((a & 0xF) + (b & 0xF)) > 0xF);
        }

        // Parity flag
        flags.setParityFlag(getParity(result & 0xFF));
    }

    // Convert unsigned 16-bit value to signed
    private int toSigned16Bit(int value) {
        value &= 0xFFFF;  // Ensure 16-bit value
        if ((value & 0x8000) != 0) {
            return value | 0xFFFF0000;  // Sign extend
        }
        return value;
    }

    // Compare two signed numbers
    public void compare(int a, int b) {
        a = toSigned16Bit(a);
        b = toSigned16Bit(b);
        subtract(a, b);  // Use subtract to set flags
    }

    private boolean getParity(int value) {
        int count = 0;
        while (value != 0) {
            count += value & 1;
            value >>>= 1;
        }
        return (count % 2) == 0;
    }
}