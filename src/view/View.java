package view;

 public class View {
    public static void printMessage(String message){
        System.out.println(message);
    }
    public static void printError(String error){
        System.err.println(error);
    }
    public static final String ERROR = "Error";
    public static final String INCORRECT_EXPR = "Incorrect expression.";
    public static final String BRACKETS_ERR = "Brackets are not the same number.";
    public static final String INPUT_FILE = "Input file name:";
    public static final String FILE_NOT_FOUND = "Error with file, file not found.";
    public static final String STRUCTURE_ERR = "Error with file, cant recognize structure of file.";
 }
