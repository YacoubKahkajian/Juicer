// This is the code which handles all the frontend functionality of the Juicer, i.e.
// setting up the window, playing the video, and drawing the colors within it. Backend
// calculations are passed onto Parse.java.

import processing.core.PApplet;
import processing.video.Movie;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class Draw extends PApplet {

    private Movie input; // Video input
    private final Queue<Color> averageColors = new LinkedList<>(); // Color storage
    private final static int WID = 960; // Width of the window
    private final static int HGT = 540; // Height of the window & color strip
    private int nthColor; // If strip must be aggregated, take every nthColor in queue
    private float duration; // Length of input, in seconds

    // Processing runs this function first. It checks the file path the user inputted
    // as a command-line argument and sets it as the input if it is valid. This method
    // is tested through the PApplet.main("Draw", args) command.
    public void setup() {
        File f = dataFile(args[0]);
        if (!f.isFile()) throw new RuntimeException("Video not found!");
        input = new Movie(this, args[0]);
        input.play();
        duration = (float) Math.floor(input.duration());
    }

    // Draws the next frame of the video if there is still time left. Otherwise, run
    // the final function and stop the loop. This method and those within it are tested
    // through the PApplet.main("Draw", args) command.
    public void draw() {
        if (input.time() >= duration) {
            finalDraw();
            noLoop();
        } else image(input, 0, 0, WID, HGT);
    }

    // Processing runs the movieEvent function when it detects a new frame in the
    // video is available. Each time this happens, the function passes the pixel
    // array to the parseVideo function.
    public void movieEvent(Movie m) {
        m.read();
        loadPixels();
        averageColors.add(Parse.parseVideo(pixels));
    }

    // Required by Processing to set the proper width and height of the window.
    public void settings() {
        size(WID, HGT);
    }

    // Function which draws the final result when the video ends.
    public void finalDraw() {
        // Remove three grey frames Processing inserts at the beginning of each video.
        averageColors.remove();
        averageColors.remove();
        averageColors.remove();

        // Get frame count and calculate the rectangle height with it.
        int frames = averageColors.size();
        double rectHeight = Parse.stripHeight(frames, HGT);

        // If rectHeight is less than 1, our video is long, and we have to
        // aggregate our colors. Use the nthColor method instead.
        if (rectHeight < 1) {
            rectHeight = -1;
            nthColor = Parse.nthColor(frames, HGT);
        }

        background(227); // Clear video
        int r = 0;
        int g = 0;
        int b = 0;
        int rectX = 0;

        // Loop to get colors in the queue.
        for (int i = 0; i < frames; i++) {
            Color current = averageColors.remove();
            fill(current.getRed(), current.getGreen(), current.getBlue());
            noStroke();

            // If we are using the nthColor method, we decide on whether we should
            // draw a color or not depending on if it is a multiple of n.
            if (rectHeight < 0 && i % nthColor == 0) {
                rectX++;
                rect(0, rectX, WID / 2, 1);
                r += current.getRed();
                g += current.getGreen();
                b += current.getBlue();
            }

            // Otherwise, just draw every color and make the rectangles a thickness
            // of rectHeight.
            else if (rectHeight >= 1) {
                rect(0, (float) (i * rectHeight), WID / 2, (float) rectHeight);
                r += current.getRed();
                g += current.getGreen();
                b += current.getBlue();
            }
        }

        float[] angl = Parse.drawChart(r, g, b);
        // Red, green, and blue for the chart stored as ints Processing translates.
        int[] RGB = {0, -1568761, -16407541, -15726616};
        double DEGREES = Math.toRadians(360);
        for (int i = 1; i < 4; i++) {
            fill(RGB[i]);
            textSize(30);
            textAlign(CENTER, CENTER);
            text(
                    Math.round(((angl[i] - angl[i - 1]) / DEGREES) * 100) + "%",
                    WID - (WID / 4),
                    (i * 50) + (HGT / 2));
            arc(WID - (WID / 4), (HGT / 2) - 50, WID / 6, HGT / 4, angl[i - 1], angl[i]);
        }
    }

    public static void main(String[] args) {
        PApplet.main("Draw", args);
    }
}
