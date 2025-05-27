package dk.easv.belmanqcreport;

// Imports
import dk.easv.belmanqcreport.BE.MyImage;
import dk.easv.belmanqcreport.BLL.UTIL.CameraHandling;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class CameraHandlingTest {

    @BeforeAll
    static void setup() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    void testGetInstance() {
        CameraHandling instance1 = CameraHandling.getInstance();
        CameraHandling instance2 = CameraHandling.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testCaptureImageWritesFile() throws Exception {
        CameraHandling handler = CameraHandling.getInstance();

        Mat dummy = Mat.eye(100, 100, CvType.CV_8UC3);
        Field frameField = CameraHandling.class.getDeclaredField("frame");
        frameField.setAccessible(true);
        frameField.set(handler, dummy);

        String outputDir = "test-output";
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        MyImage result = handler.captureImage(outputDir);

        assertNotNull(result);

        File file = new File(result.getFilePath());
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        file.delete();
        new File(outputDir).delete();

    }

}
