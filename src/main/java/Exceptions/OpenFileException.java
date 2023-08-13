package Exceptions;

public class OpenFileException extends Exception {
    private final String filename;

    public String getFilename() {
        return filename;
    }

    public OpenFileException(String filename) {
        super("File open error: " + filename);
        this.filename = filename;
    }
}
