package com.zp.android.picker.util;

import android.content.Context;

import java.io.File;

public class FileUtil {

    @Deprecated
    public static File getSaveFile(Context context) {
        return getSaveFile(context, "pic.jpg");
    }

    public static File getSaveFile(Context context, String fileName) {
        return new File(context.getFilesDir(), fileName);
    }

}
