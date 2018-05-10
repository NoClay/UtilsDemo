package pers.noclay.utiltool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringDef;

/**
 * Created by NoClay on 2018/5/10.
 */

public class ShareUtils {
    public static final String PKG_WECHAT = "com.tencent.mm";
    public static final String PKE_QQ = "com.tencent.mobileqq";

    public static final String CLS_WECHAT_FRIEND = "com.tencent.mm.ui.tools.ShareImgUI";
    public static final String CLS_WECHAT_FRIEND_CIRCLE = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
    public static final String CLS_QQ = "com.tencent.mobileqq.activity.JumpActivity";

    @StringDef({CLS_WECHAT_FRIEND, CLS_WECHAT_FRIEND_CIRCLE})
    public @interface ShareWeChat{};

    public static void shareText(Context context, String title, String content){
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(textIntent, title));
    }

    public static void shareTextToWeChat(Context context, String tile, String content){
        shareTextToPackage(context, tile, content, PKG_WECHAT);
    }

    public static void shareTextToQQ(Context context, String title, String content){
        shareTextToPackage(context, title, content, PKE_QQ);
    }

    public static void shareTextToPackage(Context context, String title, String content, String packageName){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setPackage(packageName);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    public static void shareImage(Context context, Uri imageUri){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, "分享图片"));
    }

    public static void shareImageToPackage(Context context, Uri imageUri, String packageName, String className){
        Intent shareIntent = new Intent();
        //发送图片给好友。
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        shareIntent.setComponent(comp);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    public static void shareImageToWeChat(Context context, Uri imageUri,@ShareWeChat String cls){
        shareImageToPackage(context, imageUri, PKG_WECHAT, cls);
    }

    public static void shaerImageToQQ(Context context, Uri imageUri){
        shareImageToPackage(context, imageUri, PKE_QQ, CLS_QQ);
    }
}
