package automate.tools.results;

public class Pending extends Result {
    public Pending(String message){
        info = message;
        code = 1; // Some action have been done
    }
}
