package exceptions;

public class MaintenanceRequiseException extends Exception {
    public MaintenanceRequiseException(int heures) {
        super("Maintenance requise : le robot a fonctionné " + heures + " heures sans entretien.");
    }
}
