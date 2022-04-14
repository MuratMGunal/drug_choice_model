import drug_choice.DrugChoiceModel;
import org.example.models.GaiKapadia.GaiKapadiaModel;
import org.example.models.SimudyneSIR.SimudyneSIR;
import org.example.models.TumorGrowthSimulator.TumorGrowthModel;
import org.example.models.cda.CDAModel;
import org.example.models.creditCard.CreditCardModel;
import org.example.models.forestFire.ForestFireModel;
import org.example.models.mortgage.MortgageModel;
import org.example.models.schelling.SchellingModel;
import org.example.models.tokyo.TokyoModel;
import org.example.models.trading.TradingModel;
import org.example.models.volatilityModel.VolatilityModel;
import simudyne.nexus.Server;

public class Main {
  public static void main(String[] args) {
    Server.register("Drug Choice Model", DrugChoiceModel.class);

    Server.run(args);
  }
}
