package application.body.results.entityPopulationForTableView;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EntityPopulation {
    private StringProperty entityName = new SimpleStringProperty();
    private IntegerProperty population = new SimpleIntegerProperty();

    public EntityPopulation(String name, int population){
        entityName.set(name);
        this.population.set(population);
    }

    public IntegerProperty populationProperty(){ return population;}
    public StringProperty entityNameProperty(){ return entityName;}
}
