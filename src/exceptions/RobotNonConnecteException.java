package exceptions;

public class RobotNonConnecteException extends Exception {
    public RobotNonConnecteException(String operation) {
        super("Impossible de " + operation + " : le robot n'est pas connecté à un réseau.");
    }
}
