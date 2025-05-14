package exceptions;

public class RobotEnPanneException extends RobotException {
    public RobotEnPanneException() {
        super("Le robot est actuellement éteint. Veuillez le démarrer avant d'effectuer cette action.");
    }
}
