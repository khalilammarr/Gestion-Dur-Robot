package exceptions;

public class RobotEnPanneException extends Exception {
    public RobotEnPanneException() {
        super("Le robot est actuellement éteint. Veuillez le démarrer avant d'effectuer cette action.");
    }
}

