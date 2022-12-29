# Juicer
 The Juicer generates a still art piece out of a moving image. Inspired by [The Colors of Motion](https://thecolorsofmotion.com/) project by Charlie Clark, the Juicer parses each frame of an inputted video, calculating its average color and displaying it as a color strip and a pie chart. This program was developed as a final project for the Computer Science: An Interdisciplinary Approach course at Princeton University.
 
 Below is an example of a juiced video. The final result shows the average color of roughly each frame and the distribution of red, green, and blue in the short film: [Big Buck Bunny](https://peach.blender.org/).
 
 ![An example of a juiced video.](/src/example.png)
 
 ## Installation
 To juice your own videos, download this repository and open its directory in a command line. Run the command:
 
 ```java -jar Juicer.jar "C:\video\to\juice.mp4"```
 
 Where the final argument is replaced with the file path of the video you want to process. Processing will then play the video in a new window and show you the final result when it is over.
 
 If the Juicer returns an error about running an outdated version of GStreamer, there may be an older version of GStreamer in your system environment variables which conflict with the version of the GStreamer library provided by the Processing video library. In this case, try downloading the latest version of GStreamer [here](https://gstreamer.freedesktop.org/download/). This installer should automatically add it to your PATH.