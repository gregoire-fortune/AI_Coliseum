package automate.tools.results;

public class Ok extends Result {
    public Ok(String message){
        info = message;
        code = 0;
    }
}
