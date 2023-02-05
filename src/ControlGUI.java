import java.util.ArrayList;

public class ControlGUI {
    public static void writeToClrDMDisplay(String text) {
        for (int i = 0; i < text.length(); i++) {
            IO.writeShort(0x40, text.charAt(i));
        }
    }

    public static void clrDMDisplay() {
        IO.writeShort(0x40, 0xFE);
        IO.writeShort(0x40, 0x01);
    }

    public static void setPixel(int x, int y) {
        if (y < 32 && x < 128 && y >= 0 && x >= 0) {
            IO.writeShort(0x42, 1 << 12 | x << 5 | y);
        }
    }

    public static void buildLines(int xValue, int yValue, boolean isBuildingY, boolean isBuildingX) {

        if (isBuildingY == true) {
            for (int y = 0; y < yValue; y++) {
                setPixel(xValue, y);
            }
        }

        if (isBuildingX == true) {
            for (int x = 0; x < xValue; x++) {
                setPixel(x, yValue);
            }
        }
    }

}
