package com.ypf.study.myui;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        System.out.println("ssss");
    }

    private List<VideoFile> getAllFiles(String path) {
        List<VideoFile> list = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                VideoFile videoFile = new VideoFile();
                //文件名
                String name = tempList[i].getName();
                videoFile.name = name;
            }
        }
        return list;
    }

    class VideoFile {
        String name;
        String malv;//码率
        String fenbianlv;//分辨率
        String zhenlv;//帧率
        String code;//编码格式
    }
}