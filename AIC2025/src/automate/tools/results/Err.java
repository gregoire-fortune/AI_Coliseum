package automate.tools.results;

public class Err extends Result {
    public Err(String message){
        info = message;
        code = -2;
    }
}
