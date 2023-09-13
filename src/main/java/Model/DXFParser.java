package Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DXFParser {
    private List<DXFPrimitive> dxfPrimitiveList;

    public List<DXFPrimitive> getDxfPrimitiveList() {
        return dxfPrimitiveList;
    }

    public DXFParser(String filename) throws IOException {
        this.dxfPrimitiveList = new ArrayList<>();
        List<String> dxfLines = Files.readAllLines(Paths.get(filename));
        parse(dxfLines);
    }

    private void parse(List<String> allLines) {
        List<DXFOperator> operators = new ArrayList<>();
        for (int i = 0; i < allLines.size() - 1; i += 2) {
            operators.add(new DXFOperator(allLines.get(i), allLines.get(i + 1)));
        }
        int entitiesPosition = 0;
        for (int i = 0; i < operators.size(); i++) {
            if (Integer.parseInt(operators.get(i).key().trim()) == 2 &&
                    operators.get(i).value().equals("ENTITIES")) {
                entitiesPosition = i;
                break;
            }
        }

        List<Integer> entryNodes = new ArrayList<>();

        for (int i = entitiesPosition; i < operators.size(); i++) {
            if (Integer.parseInt(operators.get(i).key().trim()) == 0 && operators.get(i).value().equals("LINE") ||
                    Integer.parseInt(operators.get(i).key().trim()) == 0 && operators.get(i).value().equals("ARC") ||
                    Integer.parseInt(operators.get(i).key().trim()) == 0 && operators.get(i).value().equals("CIRCLE")) {
                entryNodes.add(i);
            }
        }

        for (int nodeIndex = 0; nodeIndex < entryNodes.size(); nodeIndex++) {
            DXFOperator temp = operators.get(entryNodes.get(nodeIndex));
            switch (temp.value()) {
                case "LINE" -> {
                    double x1 = 0, y1 = 0, x2 = 0, y2 = 0;
                    for (int i = entryNodes.get(nodeIndex) + 1;; i++) {
                        temp = operators.get(i);
                        if (Integer.parseInt(temp.key().trim()) == 0)
                            break;
                        switch (Integer.parseInt(temp.key().trim())) {
                            case 10 -> x1 = Double.parseDouble(temp.value());
                            case 20 -> y1 = Double.parseDouble(temp.value());
                            case 11 -> x2 = Double.parseDouble(temp.value());
                            case 21 -> y2 = Double.parseDouble(temp.value());
                        }
                    }
                    dxfPrimitiveList.add(new Line(new Point(x1, y1), new Point(x2, y2)));
                }
                case "ARC" -> {
                    double x = 0, y = 0, r = 0, startAng = 0, endAng = 0;
                    for (int i = entryNodes.get(nodeIndex) + 1;; i++) {
                        temp = operators.get(i);
                        if (Integer.parseInt(temp.key().trim()) == 0)
                            break;
                        switch (Integer.parseInt(temp.key().trim())) {
                            case 10 -> x = Double.parseDouble(temp.value());
                            case 20 -> y = Double.parseDouble(temp.value());
                            case 40 -> r = Double.parseDouble(temp.value());
                            case 50 -> startAng = Double.parseDouble(temp.value());
                            case 51 -> endAng = Double.parseDouble(temp.value());
                        }
                    }
                    dxfPrimitiveList.add(new Arc(new Point(x, y), r, startAng, endAng));
                }
                case "CIRCLE" -> {
                    double x = 0, y = 0, r = 0;
                    for (int i = entryNodes.get(nodeIndex) + 1;; i++) {
                        temp = operators.get(i);
                        if (Integer.parseInt(temp.key().trim()) == 0)
                            break;
                        switch (Integer.parseInt(temp.key().trim())) {
                            case 10 -> x = Double.parseDouble(temp.value());
                            case 20 -> y = Double.parseDouble(temp.value());
                            case 40 -> r = Double.parseDouble(temp.value());
                        }
                    }
                    dxfPrimitiveList.add(new Circle(new Point(x, y), r));
                }
            }
        }
    }
}

record DXFOperator(String key, String value) {
}