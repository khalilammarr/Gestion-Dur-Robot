package exceptions;

public class MaintenanceRequiseException extends RobotException {
    public MaintenanceRequiseException(int heures) {
        super("Maintenance requise : le robot a fonctionné " + heures + " heures sans entretien.");
    }
}
