package com.github.guisaints03;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class VideoToASCII {

    private static final String VIDEO_PATH = "./src/main/resources/video.mp4";

    public static void main(String[] args) {
        OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture capture = new VideoCapture(VIDEO_PATH);
        double frameRate = capture.get(Videoio.CAP_PROP_FPS);

        double frameTime = (1000.0 / frameRate);

        double frameWidth = capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        double frameHeight = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);

        double width = 360;
        double height = ((width * frameHeight) / frameWidth) * 0.4194;

        Mat frame = new Mat();
        Mat grayFrame = new Mat();
        Mat resizedFrame = new Mat();

        while (capture.read(frame)) {
            if (frame.empty()) break;

            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
            Imgproc.resize(grayFrame, resizedFrame, new Size(width, height), 0, 0, Imgproc.INTER_AREA);

            String asciiFrame = convertToASCII(resizedFrame);
            System.out.print("\033[H\033[2J"); // Limpa o console
            System.out.flush();
            System.out.println(asciiFrame);

            try {
                Thread.sleep((long) (frameTime / 2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        capture.release();
    }

    private static String convertToASCII(Mat frame) {
        StringBuilder asciiFrame = new StringBuilder();

        for (int i = 0; i < frame.rows(); i++) {
            for (int j = 0; j < frame.cols(); j++) {
                double pixelIntensity = frame.get(i, j)[0];
                asciiFrame.append(pixelToASCII(pixelIntensity));
            }
            asciiFrame.append("\n");
        }
        return asciiFrame.toString();
    }

    static String pixelToASCII(double pixelIntensity) {
        final String ASCII_CHARS = " .-:=+*#%@$";
        String s = "";
        s += ASCII_CHARS.charAt((int) (pixelIntensity * ASCII_CHARS.length() / 256));
        return s;
    }
}

