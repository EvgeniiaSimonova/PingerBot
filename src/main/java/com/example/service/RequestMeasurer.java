package com.example.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RequestMeasurer {

    public static long getTimeOfRequest(String stringUrl) throws IOException {
        long result;

        URL url = new URL(stringUrl);
        try (InputStream inputStream = url.openStream()) {
            long start = System.currentTimeMillis();
            while (inputStream.read() != -1) {}
            long end = System.currentTimeMillis();
            result = end - start;
        }

        return result;
    }
}
