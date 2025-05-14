package exceptions;

public class RobotException extends Exception {
    public RobotException(String message) {
        super(message); // Transmet le message à la classe parente Exception
    }
}
