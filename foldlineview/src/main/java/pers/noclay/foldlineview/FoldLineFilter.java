package pers.noclay.foldlineview;

import java.util.List;

/**
 * Created by i-gaolonghai on 2017/8/16.
 */

interface FoldLineFilter {

    int getCount();
    List<Float> getItemPoint(int position);
    String getItemLabel(int position);
}
