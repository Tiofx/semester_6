package lab4.util.algorithm;


import java.util.Arrays;

public class FloodFiller {
    int width = 600, height = 600;

    public static final int rgbNumber = 3;

    float tempCoordinate[] = new float[2];
    float tempColor[] = new float[3];
    float newColor[] = new float[3];
    private float[] screenBuffer;

    protected void setColor(int x, int y, float[] color) {

        screenBuffer[(y * width + x) * rgbNumber + 0] = color[0];
        screenBuffer[(y * width + x) * rgbNumber + 1] = color[1];
        screenBuffer[(y * width + x) * rgbNumber + 2] = color[2];
    }


    protected float[] getColor(int x, int y) {
        tempColor[0] = screenBuffer[(y * width + x) * rgbNumber + 0];
        tempColor[1] = screenBuffer[(y * width + x) * rgbNumber + 1];
        tempColor[2] = screenBuffer[(y * width + x) * rgbNumber + 2];

        return tempColor;
    }

    public synchronized void floodFillScanline(int x, int y, float[] newColor) {
        if (Arrays.equals(getColor(x, y), newColor)) return;
        this.newColor = newColor;

        int xLeft = x, xRight = x + 1;

        while (xRight < width && !Arrays.equals(getColor(xRight, y), newColor)) {
            setColor(xRight, y, newColor);
            xRight++;
        }

        while (xLeft >= 0 && !Arrays.equals(getColor(xLeft, y), newColor)) {
            setColor(xLeft, y, newColor);
            xLeft--;
        }

        xRight--;
        xLeft++;

        if (!(xLeft > xRight)) {
            floodFillScanline((xRight + xLeft) / 2, y - 1, false, newColor);
            floodFillScanline((xRight + xLeft) / 2, y + 1, true, newColor);
        }
    }

    public void floodFillScanline(int x, int y, boolean up, float[] newColor) {
        if (Arrays.equals(getColor(x, y), newColor)) return;
        this.newColor = newColor;

        int xLeft = x, xRight = x + 1;

        while (xRight < width && !Arrays.equals(getColor(xRight, y), newColor)) {
            setColor(xRight, y, newColor);
            xRight++;
        }

        while (xLeft >= 0 && !Arrays.equals(getColor(xLeft, y), newColor)) {
            setColor(xLeft, y, newColor);
            xLeft--;
        }

        xRight--;
        xLeft++;

        if (!(xLeft > xRight)) {
            floodFillScanline((xRight + xLeft) / 2, y + (up ? 1 : -1), newColor);
        }
    }

    public void setScreenBuffer(float[] screenBuffer) {
        this.screenBuffer = screenBuffer;
    }

    public float[] getScreenBuffer() {
        return screenBuffer;
    }
}