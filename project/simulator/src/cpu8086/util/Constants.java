package cpu8086.util;

public final class Constants {
    private Constants() {} // Prevent instantiation

    public static final int MEMORY_SIZE = 1048576; // 1MB
    public static final int MAX_INSTRUCTION_SIZE = 6;
    public static final int REGISTER_SIZE = 16; // 16 bits

    public static final String[] REGISTER_NAMES = {
        "AX", "BX", "CX", "DX", "SI", "DI", "BP", "SP"
    };

    public static final String[] SEGMENT_REGISTER_NAMES = {
        "CS", "DS", "SS", "ES"
    };
}