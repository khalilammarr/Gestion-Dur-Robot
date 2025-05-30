package exceptions;

public class RobotNonConnecteException extends RobotException {
  public RobotNonConnecteException(String operation) {
    super("Impossible de " + operation + " : le robot n'est pas connecté à un réseau.");
  }

  public RobotNonConnecteException() {
    super("Le robot n'est pas connecté à un réseau.");
  }
}
