package dk.easv.belmanqcreport;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import java.util.concurrent.CountDownLatch;

public class JavaFxInitializer {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            // Required to trigger JavaFX init
            new JFXPanel(); // triggers JavaFX initialization in tests
            latch.countDown();
        });

        try {
            latch.await(); // wait for JavaFX to start
            initialized = true;
        } catch (InterruptedException e) {
            throw new RuntimeException("JavaFX init failed", e);
        }
    }
}
