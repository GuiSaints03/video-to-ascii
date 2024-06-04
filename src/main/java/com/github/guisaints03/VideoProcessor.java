package com.github.guisaints03;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VideoProcessor {

    private static final double CUSTOM_WIDTH = 360;

    private VideoProcessor() {
    }

    public static void processVideo(String videoPath) {
        VideoCapture capture = new VideoCapture(videoPath);

        if (!capture.isOpened()) {
            System.err.println("Invalid path or corrupted video");
            return;
        }

        double frameRate = capture.get(Videoio.CAP_PROP_FPS);
        double frameTime = (1000.0 / frameRate);
        double frameWidth = capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        double frameHeight = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        double customHeight = ((CUSTOM_WIDTH * frameHeight) / frameWidth) * 0.4194;

        Mat frame = new Mat();
        Mat resizedFrame = new Mat();

        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {

            while (capture.read(frame)) {
                if (frame.empty()) break;

                Imgproc.resize(frame, resizedFrame, new Size(CUSTOM_WIDTH, customHeight), 0, 0, Imgproc.INTER_AREA);

                executor.submit(() -> {
                    String asciiFrame = ASCIIGenerator.convertToASCII(resizedFrame);
                    synchronized (System.out) {
                        ConsoleUtils.clearConsole();
                        System.out.println(asciiFrame);
                    }
                });

                try {
                    Thread.sleep((long) frameTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            capture.release();
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
