public class Contract {
    int duration; // длительность контракта
    double[] costsFishFor3Week; // стоимость рыбы на каждые 3 недели контракта
    double[] countsFishFor3Week; // стоимость рыбы на каждые 3 недели контракта
    double[] costsFoodFor3Week; // стоимость корма на каждые 3 недели контракта
    double penalty; // неустойка
    double mustBuyFoodFor1Week; // на какую сумму должен скупить еды
    int mustSellFishFor1week_Kg; // сколько в неделю должен поставить кг рыбы

    public Contract(int duration, double mustBuyFoodFor1Week, int mustSellFishFor1week_Kg,
     double[] costsFishFor3Week, double[] costsFoodFor3Week, double penalty){
        this.duration = duration;
        this.mustBuyFoodFor1Week = mustBuyFoodFor1Week;
        this.mustSellFishFor1week_Kg = mustSellFishFor1week_Kg;
        this.costsFishFor3Week = costsFishFor3Week;
        this.costsFoodFor3Week = costsFoodFor3Week;
        this.penalty = penalty;
    }

}
