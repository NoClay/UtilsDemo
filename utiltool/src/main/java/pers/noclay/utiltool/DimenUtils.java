package pers.noclay.utiltool;

import android.content.Context;

/**
 * Created by i-gaolonghai on 2017/8/8.
 */

public class DimenUtils {
    /**
     * dp到px转换
     * @param context
     * @param dp
     * @return
     */
    public static int getPxFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px到dp的转换
     * @param context
     * @param px
     * @return
     */
    public static int getDpFromPx(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
