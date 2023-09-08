package Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DXFParser {
    private List<DXFPrimitive> dxfPrimitiveList;

    public DXFParser(String filename) throws IOException {
        this.dxfPrimitiveList = new ArrayList<>();
        List<String> dxfLines = Files.readAllLines(Paths.get(filename));
        parse(dxfLines);
    }

    private void parse(List<String> allLines) {

    }
}
