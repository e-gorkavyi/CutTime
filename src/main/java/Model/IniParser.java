package Model;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class IniParser {
    public static Map<String, Map<String, String>> parse(File fileToParse) throws IOException {
        Ini ini = new Ini(fileToParse);
        return ini.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
