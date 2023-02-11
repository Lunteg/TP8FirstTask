public class FishKoef{
    double koefBirn; // коэффициент рождаемости
    double koefSurvival; // коэффициент выживаемости
    double koefDie; // коэф смертности 

    public FishKoef(double koefBirn, double koefSurvival, double koefDie){
        this.koefBirn = koefBirn;
        this.koefSurvival = koefSurvival;
        this.koefDie = koefDie;
    }
}