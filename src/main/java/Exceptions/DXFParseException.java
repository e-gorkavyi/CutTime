package Exceptions;

public class DXFParseException extends Exception {
    private final String filename;

    public String getFilename() {
        return filename;
    }

    public DXFParseException(String filename) {
        super("DXF parsing exception. File: " + filename);
        this.filename = filename;
    }
}
