package Exceptions;

import java.io.IOException;

public class OpenFileException extends IOException {
    private final String filename;

    public String getFilename() {
        return filename;
    }

    public OpenFileException(String filename) {
        super("File open error: " + filename);
        this.filename = filename;
    }
}
