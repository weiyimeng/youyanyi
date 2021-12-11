package com.uyu.device.devicetraining.presentation.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by xiaoban  Ubuntu on 16-1-13.
 */
public class ThrowableUtils {

    public static String getExceptionBody(Throwable ex){

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        ex.printStackTrace();

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = writer.toString();
        printWriter.close();

        return result;
    }
}
