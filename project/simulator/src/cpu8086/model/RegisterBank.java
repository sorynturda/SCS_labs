package cpu8086.model;

public class RegisterBank {
    private static final int MAX_16BIT = 0xFFFF;  // Maximum value for 16-bit register: 65535
    private int[] generalPurpose;
    private int[] segment;
    private int ip;
    private int flags;

    public RegisterBank() {
        generalPurpose = new int[8]; // AX, BX, CX, DX, SI, DI, BP, SP
        segment = new int[4]; // CS, DS, SS, ES
        reset();
    }

    public void reset() {
        for(int i = 0; i < generalPurpose.length; i++) {
            generalPurpose[i] = 0;
        }
        for(int i = 0; i < segment.length; i++) {
            segment[i] = 0;
        }
        ip = 0;
        flags = 0;
    }

    public int getRegister(RegisterType type) {
        switch(type) {
            case AX: return generalPurpose[0];
            case BX: return generalPurpose[1];
            case CX: return generalPurpose[2];
            case DX: return generalPurpose[3];
            case SI: return generalPurpose[4];
            case DI: return generalPurpose[5];
            case BP: return generalPurpose[6];
            case SP: return generalPurpose[7];
            case CS: return segment[0];
            case DS: return segment[1];
            case SS: return segment[2];
            case ES: return segment[3];
            case IP: return ip;
            default: throw new IllegalArgumentException("Invalid register type");
        }
    }

    public void setRegister(RegisterType type, int value) {
        // Clamp value to maximum 16-bit value if it overflows
        int clampedValue = (value > MAX_16BIT) ? MAX_16BIT : (value & 0xFFFF);

        switch(type) {
            case AX: generalPurpose[0] = clampedValue; break;
            case BX: generalPurpose[1] = clampedValue; break;
            case CX: generalPurpose[2] = clampedValue; break;
            case DX: generalPurpose[3] = clampedValue; break;
            case SI: generalPurpose[4] = clampedValue; break;
            case DI: generalPurpose[5] = clampedValue; break;
            case BP: generalPurpose[6] = clampedValue; break;
            case SP: generalPurpose[7] = clampedValue; break;
            case CS: segment[0] = clampedValue; break;
            case DS: segment[1] = clampedValue; break;
            case SS: segment[2] = clampedValue; break;
            case ES: segment[3] = clampedValue; break;
            case IP: ip = clampedValue; break;
            default: throw new IllegalArgumentException("Invalid register type");
        }
    }
}