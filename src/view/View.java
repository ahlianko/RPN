package view;

/**
 * This class stores constant values, String and messages
 *
 */
 public class View {
    public static void printMessage(String message){
        System.out.println(message);
    }
    public static void printError(String error){
        System.err.println(error);
    }
    public static final String ERROR = "Error";
     public static final String INPUT_FILE = "Input file name:";
    public static final String FILE_NOT_FOUND = "Error with file, file not found.";
    public static final String STRUCTURE_ERR = "Error with file, wrong structure of file.";
 }
