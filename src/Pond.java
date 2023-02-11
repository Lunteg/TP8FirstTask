public class Pond {
    int numOfYoungFish; // число молодняка
    int numOfOldFish; // число взрослых рыб
    FishKoef fishKoef; // числовые коэфы для этого вида

    static int sumNumOfYoungFish = 0;
    static int sumNumOfOldFish = 0;

    public Pond(int numOfYoungFish, int numOfOldFish,
     String fishName // название рыбы, которая разводится в этом пруду
     ){
        this.numOfYoungFish = numOfYoungFish;
        sumNumOfYoungFish += numOfYoungFish;
        this.numOfOldFish = numOfOldFish;
        sumNumOfOldFish += numOfOldFish;
        this.fishKoef = App.fishKoef.get(fishName);
    }

    public void Step(){
        numOfOldFish = (int)(fishKoef.koefSurvival * numOfYoungFish - fishKoef.koefDie * numOfOldFish);
        numOfYoungFish = (int)(fishKoef.koefBirn * numOfOldFish);
    }

}