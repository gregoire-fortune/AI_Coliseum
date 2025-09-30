package automate.tools.results;

public class Lock extends Result {
    public Lock(String message){
        info = message;
        code = 2; // Stay in the same state
    }
}
