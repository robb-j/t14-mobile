package com.jackissogreat;

/**
 * Created by Jack on 16/02/2015.
 *
 * This isn't actually being used right now but should let me transition with updates easier if I need to
 */
import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;

import java.io.File;

public class RobolectricGradleTestRunner extends RobolectricTestRunner {
    private static final int MAX_SDK_SUPPORTED_BY_ROBOLECTRIC = 18;

    public RobolectricGradleTestRunner(Class<?> testClass) throws InitializationError {

        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        /*
        String manifestProperty = "../app/src/main/AndroidManifest.xml";
        String resProperty = "../app/src/main/res";

        return new AndroidManifest(Fs.fileFromPath(manifestProperty), Fs.fileFromPath(resProperty)) {
            @Override
            public int getTargetSdkVersion() {
                return MAX_SDK_SUPPORTED_BY_ROBOLECTRIC;
            }
        };*/


        String appModuleName = "app";
        String userDir = System.getProperty("user.dir", "./");
        File current = new File(userDir);
        String prefix;
        if (new File(current, appModuleName).exists()) {
            System.out.println("Probably running on AndroidStudio");
            prefix = "./" + appModuleName;
        }
        else if (new File(current.getParentFile(), appModuleName).exists()) {
            System.out.println("Probably running on Console");
            prefix = "../" + appModuleName;
        }
        else {
            throw new IllegalStateException("Could not find app module, app module should be \"app\" directory in the project.");
        }

        System.setProperty("android.manifest", prefix + "/src/main/AndroidManifest.xml");
        System.setProperty("android.resources", prefix + "/src/main/res");
        System.setProperty("android.assets", prefix + "/src/main/assets");

        return super.getAppManifest(config);
    }
}
