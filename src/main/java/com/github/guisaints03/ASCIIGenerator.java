package com.github.guisaints03;

import org.opencv.core.Mat;

public class ASCIIGenerator {

    private static final String ASCII_CHARS = " .:-=+*#%P@";
    private static final char[] ASCII_MAP = new char[256];

    static {
        // maps pixel brightness values to their corresponding ASCII characters.
        for (int i = 0; i < 256; i++) {
            ASCII_MAP[i] = ASCII_CHARS.charAt(i * ASCII_CHARS.length() / 256);
        }
    }

    private ASCIIGenerator() {}

    public static String convertToASCII(Mat frame) {
        StringBuilder asciiFrame = new StringBuilder((int) (frame.rows() * frame.cols() * .1)); // 1.1 to append \n without relocating
        int rows = frame.rows();
        int cols = frame.cols();
        byte[] pixelData = new byte[cols * 3]; // BGR

        for (int i = 0; i < rows; i++) {
            frame.get(i, 0, pixelData);
            for (int j = 0; j < cols; j++) {
                int b = Byte.toUnsignedInt(pixelData[j * 3]);
                int g = Byte.toUnsignedInt(pixelData[j * 3 + 1]);
                int r = Byte.toUnsignedInt(pixelData[j * 3 + 2]);
                asciiFrame.append(getColoredASCII(r, g, b));
            }
            asciiFrame.append("\n");
        }
        return asciiFrame.toString();
    }

    private static String getColoredASCII(int r, int g, int b) {
        return "\033[38;2;" + r + ";" + g + ";" + b + "m" + ASCII_MAP[(r + g + b) / 3];
    }
}
