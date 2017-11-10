package io.github.jokoframework.mboehaolib.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

import io.github.jokoframework.mboehaolib.constants.Constants;

/**
 * Created by joaquin on 23/08/17.
 */

public class ImageUtils {

    private static final String DEFAULT_FONT_SIZE = "2";
    private static final String HD_FONT_SIZE = "3";
    private static final String TABLET_FONT_SIZE = "4";
    private static Map<Integer, Integer> warningPoints = new HashMap<>();

    private ImageUtils() {
    }
    
    public static Point getScreenSizeInPixels(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static String getHtmlScaledFontSize(Context context) {
        String fontSize = DEFAULT_FONT_SIZE;
        Point screenSize = getScreenSizeInPixels(context);
        if (screenSize != null) {
            if (Constants.TABLET_VERTICAL_RESOLUTION >= screenSize.y) {
                fontSize = TABLET_FONT_SIZE;
            } else if (Constants.HD_VERTICAL_RESOLUTION >= screenSize.y) {
                fontSize = HD_FONT_SIZE;
            }
        }
        return fontSize;
    }
}
