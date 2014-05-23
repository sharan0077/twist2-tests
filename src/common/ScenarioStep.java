package common;

import com.thoughtworks.gauge.Table;

import java.util.ArrayList;
import java.util.List;

public class ScenarioStep {
    private String name;

    public List<String> parameters = new ArrayList<String>();
    public Table table;

    public ScenarioStep(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTable(Table table){
        this.table = table;
    }

    public Table getTable() {
        return table;
    }
}
