package pers.noclay.util.data;

import android.content.Intent;

/**
 * Created by i-gaolonghai on 2017/8/9.
 */

public class Demo {
    private String title;
    private Intent intent;

    public Demo(String title, Intent intent) {
        this.title = title;
        this.intent = intent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
