package pers.noclay.foldlineview;


import java.util.List;

/**
 * Created by i-gaolonghai on 2017/8/16.
 */

public class FoldLineAdapter<T> implements FoldLineFilter {
    private List<T> mTList;

    public FoldLineAdapter(List<T> TList) {
        mTList = TList;
    }

    @Override
    public int getCount() {
        return mTList.size();
    }

    @Override
    public List<Float> getItemPoint(int position) {
        if (position < mTList.size() || position >= 0 && mTList.get(position) instanceof FoldLineInterface){
            return ((FoldLineInterface) mTList.get(position)).getLinesAsList();
        }
        return null;
    }

    @Override
    public String getItemLabel(int position) {
        if (position < mTList.size() || position >= 0 && mTList.get(position) instanceof FoldLineInterface){
            return ((FoldLineInterface) mTList.get(position)).getLabel();
        }
        return null;
    }
}
