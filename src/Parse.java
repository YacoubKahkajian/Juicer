// Parse handles the calculations necessary to draw the final result accurately,
// including the average color of each frame of a video and the angles which are drawn
// in the RGB pie chart.

import java.awt.*;

public class Parse {

    // Given an int array of int values representing RGB color codes, calculate the
    // average color code of that array and return a color value representing it.
    public static Color parseVideo(int[] pixels) {
        int r = 0;
        int g = 0;
        int b = 0;
        int l = pixels.length;

        // Extract the color values from the int using bit shifting and masking, as
        // described in Processing's docs. https://processing.org/reference/blue_.html
        for (int pixel : pixels) {
            r += pixel >> 16 & 0xFF;
            g += pixel >> 8 & 0xFF;
            b += pixel & 0xFF;
        }
        return new Color(r / l, g / l, b / l);
    }

    // Given counts of total red, green, and blue values across the average colors
    // drawn in the window, return the angles of a pie chart which shows the
    // proportion of each color used.
    public static float[] drawChart(int r, int g, int b) {
        float[] angles = new float[4];
        int[] p = {0, r, g, b}; // p for parameters
        double total = r + g + b;
        int DEGS = 360;
        for (int i = 1; i < 4; i++)
            angles[i] = angles[i - 1] + (float) Math.toRadians((p[i] / total * DEGS));
        return angles;
    }

    // Return the height of each color strip given a video frame count and window
    // height. If frames is equal to zero, we assume Processing had trouble parsing
    // the video since it was too short and might have skipped over the first few
    // frames.
    public static double stripHeight(int frames, int height) {
        if (frames <= 0) throw new RuntimeException(
                "Your video is too short! Try juicing something a bit longer.");
        return (double) height / (double) frames;
    }

    // Return which frames should be drawn in the final color visualization and
    // which should be excluded given a frame count and window height.
    public static int nthColor(int frames, int height) {
        return frames / height;
    }
}
