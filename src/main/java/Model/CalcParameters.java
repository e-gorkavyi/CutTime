package Model;

import java.io.File;

public class CalcParameters {
    private final PlotterHead plotterHead;
    private final File dxfFile;

    public CalcParameters(PlotterHead plotterHead, File dxfFile) {
        this.plotterHead = plotterHead;
        this.dxfFile = dxfFile;
    }

    public PlotterHead getPlotterHead() {
        return plotterHead;
    }

    public File getDxfFile() {
        return dxfFile;
    }
}
