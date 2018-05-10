package pers.noclay.utiltool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by i-gaolonghai on 2017/8/8.
 */

public class BitmapUtils {
    /**
     * 将布局转化为bitmap这里传入的是你要截的布局的根View
     */
    public static Bitmap getBitmapByView(View headerView) {
        int h = headerView.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(headerView.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        headerView.draw(canvas);
        return bitmap;
    }

    /**
     * 压缩图片
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 10, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 400) {  //循环判断如果压缩后图片是否大于400kb,大于继续压缩（这里可以设置大些）
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 将Bitmap保存到指定的文件
     *
     * @param bm       bitmap对象
     * @param filePath 路径
     * @param quality  图片的质量，0-100，不含0,100
     * @return 返回一个File对象，如果路径错误，返回null
     */
    public static File saveBitmapToFile(Bitmap bm, String filePath, int quality) {
        File f;
        if (filePath == null) {
            return null;
        } else {
            f = new File(filePath);
        }
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            if (filePath.endsWith(".jpg") || filePath.endsWith("jpeg")) {
                bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            } else if (filePath.endsWith(".png")) {
                bm.compress(Bitmap.CompressFormat.PNG, quality, out);
            } else if (filePath.endsWith(".webp")) {
                bm.compress(Bitmap.CompressFormat.WEBP, quality, out);
            } else {
                return null;
            }
            out.flush();
            out.close();
            bm.recycle();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取资源转换为bitmap
     *
     * @param context
     * @param id      资源id
     * @return
     */
    public static Bitmap getBitmapFromResourceId(Context context, int id) {
        return BitmapFactory.decodeResource(context.getResources(), id);
    }


    /**
     * 获取带倒影的bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getBitmapWithReflectionImage(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 获取圆角bitmap
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 缩放一个bitmap
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getScaledBitmap(Bitmap bitmap, int width, int height) {
        int targetWidth = bitmap.getWidth();
        int targetHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / targetWidth);
        float scaleHeight = ((float) height / targetHeight);
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, targetWidth, targetHeight, matrix, true);
    }

}
