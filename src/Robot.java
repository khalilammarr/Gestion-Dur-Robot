import java.util.ArrayList;
import java.util.List;
public abstract class Robot {
    private int x;
    private int y;
    private String id;
    public int heuresUtilisation=0;
    private int energie;
    public boolean enMarche=false;
    protected List<String> historiqueActions;
    public Robot(int x, int y, String id,int energie) {
        this.x = x;
        this.y = y;
        this.id=id;
        this.energie=energie;
        this.historiqueActions=new ArrayList<>();
        this.ajouterHistorique("Robot Créé");
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getId() {
        return id;
    }
    public int getEnergie() {
        return energie;
    }
    public void ajouterHistorique(String nom) {
        //Nappendiw  l wakt wl date wl message eli je maa l fonction lel liste historiqueActions,
    }
    public void verifierEnergie(int energierequise){
        if(energierequise>this.energie){
            //on leve une exception : energie insuffisante
        }
    }
    public void verifierMaintenance(){
        if(this.heuresUtilisation>=100){
            //on leve un exception maintenance requise
        }
    }
    public void demarrer(){

        if(energie<10){
            //on leve exception ManqueEnergie
        }
        else{
            enMarche=true;
            // l energie tonkes wala le ya zabri
            ajouterHistorique("Démarrage Du Robot");
        }

    }
    public void arreter(){
        enMarche=false;
        ajouterHistorique("Arret Du Robot");
    }
    public void consommer(int quantite){
        int temp=energie-quantite;
        if(temp<=0){
            energie=0;
        }
        else{
            energie=temp;
        }
    }
    public
}
