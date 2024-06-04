package com.github.guisaints03;

import org.opencv.core.Core;

public class OpenCVLoader {

    private OpenCVLoader() {}

    public static void loadLibraries() {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
