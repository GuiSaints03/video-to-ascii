package com.github.guisaints03;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class VideoToASCII {

    private static final String ASCII_CHARS = ".-:=+*%#@$";
    private static final String VIDEO_PATH = "./src/main/resources/video6.mp4";

    public static void main(String[] args) {
        loadLibraries();
        processVideo();
    }

    private static void loadLibraries() {
        OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static void processVideo() {
        VideoCapture capture = new VideoCapture(VIDEO_PATH);

        if (!capture.isOpened()) {
            System.err.println("Invalid path or corrupted video");
            return;
        }

        double frameRate = capture.get(Videoio.CAP_PROP_FPS);
        double frameTime = (1000.0 / frameRate);
        double frameWidth = capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        double frameHeight = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        double customWidth = 240;
        double customHeight = ((customWidth * frameHeight) / frameWidth) * 0.4194;

        Mat frame = new Mat();
        Mat resizedFrame = new Mat();

        while (capture.read(frame)) {
            if (frame.empty()) break;

            Imgproc.resize(frame, resizedFrame, new Size(customWidth, customHeight), 0, 0, Imgproc.INTER_AREA); // Subsampling

            String asciiFrame = convertToASCII(resizedFrame);
            System.out.print("\033[H\033[2J"); // Clear console
            System.out.flush();
            System.out.println(asciiFrame);

            try {
                Thread.sleep((long) frameTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        capture.release();
    }

    private static String convertToASCII(Mat frame) {
        StringBuilder asciiFrame = new StringBuilder((int) (frame.rows() * frame.cols() * 1.1));
        for (int i = 0; i < frame.rows(); i++) {
            for (int j = 0; j < frame.cols(); j++) {
                double[] pixel = frame.get(i, j);
                asciiFrame.append(printColored(pixel));
            }
            asciiFrame.append("\n");
        }
        return asciiFrame.toString();
    }

    private static char pixelToASCII(double pixel) {
        return ASCII_CHARS.charAt((int) (pixel * ASCII_CHARS.length() / 256));
    }

    private static String printColored(double[] pixel) {
        int r = (int) pixel[2];
        int g = (int) pixel[1];
        int b = (int) pixel[0];
        return "\033[38;2;" + r + ";" + g + ";" + b + "m" + pixelToASCII(pixel[0]);
    }
}
