package exceptions;

public class EnergieInsuffisanteException extends RobotException {
    public EnergieInsuffisanteException(int requise, int disponible) {
        super("Énergie insuffisante : requise = " + requise + "%, disponible = " + disponible + "%.");
    }

    public EnergieInsuffisanteException(String operation, int requise, int disponible) {
        super("Impossible de " + operation + " : énergie requise = " + requise + "%, disponible = " + disponible + "%.");
    }
}
