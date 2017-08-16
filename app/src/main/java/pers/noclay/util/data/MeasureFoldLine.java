package pers.noclay.util.data;

import java.util.ArrayList;
import java.util.List;

import pers.noclay.foldlineview.FoldLineBean;
import pers.noclay.foldlineview.FoldLineInterface;

/**
 * Created by i-gaolonghai on 2017/8/14.
 */

public class MeasureFoldLine implements FoldLineInterface{
    private Float averageData;
    private Float maxData;
    private Float minData;
    private String label;

    public MeasureFoldLine(Float averageData, Float maxData, Float minData, String label) {
        this.averageData = averageData;
        this.maxData = maxData;
        this.minData = minData;
        this.label = label;
    }

    @Override
    public List<Float> getLinesAsList() {
        List<Float> data = new ArrayList<>();
        data.add(averageData);
        data.add(maxData);
        data.add(minData);
        return data;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
