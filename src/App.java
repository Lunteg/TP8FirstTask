//import java.io.Console;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    // словарь коэфов рыб в зависимости от вида
    static HashMap<String, FishKoef> fishKoef = new HashMap<String, FishKoef>();
    static int numOfPonds; //Число прудов
    static Pond[] ponds;//массив прудов

    /* Заполняем всякие коэфы рыб*/
    public static void createKoef(){
        fishKoef.put("osetr", new FishKoef(5, 6, 7));
        fishKoef.put("treska", new FishKoef(2, 7, 7));
        fishKoef.put("shuka", new FishKoef(4, 2, 3));
        fishKoef.put("forel", new FishKoef(3, 9, 15));
        fishKoef.put("karp", new FishKoef(2, 9, 10));
    }

    /*Блок инициации прудов */
    public static void createPounds(Scanner in){
        System.out.print("------------------------------");

        ponds = new Pond[numOfPonds];
        for (int i = 0; i < numOfPonds; i++){
            System.out.print("Пруд " + i);

            System.out.print("Число молодняка :");
            int numOfYoungFish = in.nextInt();
            System.out.print("Число взрослых :");
            int numOfOldFish = in.nextInt();
            System.out.print("Название вида :");
            String fishName = in.next();
            Pond pond_i = new Pond(numOfYoungFish, numOfOldFish, fishName);
            ponds[i] = pond_i;
            System.out.print("-----------------------");
        }
    }
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);

        createKoef();

        System.out.print("---------------------- \n Начальные параметры");
        System.out.print("Число прудов :");
        numOfPonds = in.nextInt();

        System.out.print("Денежный капитал:");
        double cashCapital = in.nextInt(); // Денежный капитал
        

        createPounds(in);

        System.out.print("длительность контракта:");
        int contractDuration = in.nextInt(); // длительность контракта
        System.out.print("Обязан купить еды в неделю (кг):");
        double mustBuyFoodFor1Week = in.nextInt();
        System.out.print("Обязан продать рыбы в неделю (кг):");
        int mustSellFishFor1week_Kg = in.nextInt();

        double[] costsFishFor3Week = new double[contractDuration / 3];
        double[] costsFoodFor3Week = new double[contractDuration / 3];
        for (int i = 0 ; i < contractDuration / 3; i++){
            System.out.print("Стоимость рыбы для " + (i+1) + "го промежутка по 3 недели:"); 
            costsFishFor3Week[i] = in.nextInt();
            System.out.print("Стоимость корма для " + (i+1) + "го промежутка по 3 недели:"); 
            costsFoodFor3Week[i] = in.nextInt();
        }
        
        System.out.print("Штраф за непроданный кг рыбы: "); 
        double penaltyForKg = in.nextInt(); // штраф за непроданный килограмм
        System.out.print("------------------ "); 

        in.close();

        Contract startContract = new Contract(contractDuration, mustBuyFoodFor1Week,
            mustSellFishFor1week_Kg, costsFishFor3Week, costsFoodFor3Week, penaltyForKg);

        
        // Идея - храним число рыбы которое может потратить - и из этого процентно делим

        double foodStorage = 0; // сколько осталось на складе (поскольку закупаем по контракту - может остаться)
        double notCellFish = startContract.mustSellFishFor1week_Kg; // сколько рыбов не продали
        for (int i = 0; i < contractDuration; i++){
            double foodCostForKG = startContract.costsFishFor3Week[i / 3]; // стоимость корма на этой итерации
            for (Pond p : ponds) {

                double needBuyFoodFor1Week = foodCostForKG * p.numOfYoungFish / 2 + p.numOfOldFish;
                /*работа со складом */
                if (foodStorage > needBuyFoodFor1Week){
                    needBuyFoodFor1Week = 0;
                    foodStorage -= needBuyFoodFor1Week;
                }
                else {
                    needBuyFoodFor1Week -= foodStorage;
                    foodStorage = 0;
                }

                /*работа с едой по контракту */
                double deltaBuyFood = mustBuyFoodFor1Week - needBuyFoodFor1Week;
                cashCapital -= mustBuyFoodFor1Week; // заплатим за еду
                if (deltaBuyFood >= 0){ // остаются запасы
                    
                    foodStorage += deltaBuyFood / foodCostForKG;
                }
                else { // иначе гибель части
                    double percentDie = (deltaBuyFood * -1.0) / needBuyFoodFor1Week / 2.0;
                    p.numOfOldFish -=  p.numOfOldFish * percentDie;
                    p.numOfYoungFish -= p.numOfYoungFish * percentDie;
                }

                /*работа с рыбовыми */
                /* Идея: зададим процент того, сколько максимум можно вытащить - затем начнем насчитывать
                 * процент относительно общего числа по контракту - если не смогли столько вытащить - пересчитываем
                 */
                final double maxPercOfYoung = 0.5; // максимальный процент молодой рыбы, который можно выловить
                final double maxPercOfOld = 0.8;
                
                double percNumOfYoungFish = p.numOfYoungFish / Pond.sumNumOfYoungFish;//процент молодой рыбы от общей
                double percNumOfOldFish = p.numOfOldFish / Pond.sumNumOfOldFish;//процент старой рыбы от общей
                
                double curCanCellYoung = startContract.mustSellFishFor1week_Kg * percNumOfYoungFish; // сколько должен продать молодняка
                double curCanCellOld = startContract.mustSellFishFor1week_Kg * percNumOfOldFish;
                // если больше, чем положено
                if (curCanCellYoung / p.numOfYoungFish > maxPercOfYoung){
                    p.numOfYoungFish -= p.numOfYoungFish * maxPercOfYoung;
                    notCellFish -= p.numOfYoungFish * maxPercOfYoung;
                    cashCapital += p.numOfYoungFish * maxPercOfYoung * startContract.costsFishFor3Week[i/3];
                }
                else{
                    p.numOfYoungFish -= curCanCellYoung;
                    notCellFish  -= curCanCellYoung;
                    cashCapital += curCanCellYoung * startContract.costsFishFor3Week[i/3];
                }

                if (curCanCellOld / p.numOfOldFish > maxPercOfOld){
                    p.numOfOldFish -= p.numOfOldFish * maxPercOfOld;
                    notCellFish -= p.numOfOldFish * maxPercOfOld;
                    cashCapital += p.numOfOldFish * maxPercOfOld * startContract.costsFishFor3Week[i/3];
                }
                else{
                    p.numOfOldFish -= curCanCellOld;
                    notCellFish  -= curCanCellOld;
                    cashCapital += curCanCellOld * startContract.costsFishFor3Week[i/3];
                }
                p.Step(); // шаг моделирования
            }
            if (notCellFish > 0){ // штрафуем за недопродажу
                cashCapital -= notCellFish * startContract.penalty;
            }
            if (cashCapital < 0){
                System.out.println("Обанкротились :(");
                break;
            }
            else{
                System.out.println("После недели " + i + "на счету (руб): " + cashCapital);
            }

        }

    }
}
