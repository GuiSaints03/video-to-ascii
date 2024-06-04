package com.github.guisaints03;

public class VideoToASCII {

    private static final String VIDEO_PATH = "./src/main/resources/video6.mp4";

    public static void main(String[] args) {
        OpenCVLoader.loadLibraries();
        VideoProcessor.processVideo(VIDEO_PATH);
    }
}
