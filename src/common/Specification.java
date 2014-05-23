package common;

import com.thoughtworks.gauge.Table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Specification {

    private String name;
    private List<Scenario> scenarios = new ArrayList<Scenario>();
    private List<ScenarioStep> contextSteps = new ArrayList<ScenarioStep>();
    private File specFile = null;
    private Table dataTable;

    public Table getDataTable() {
        return dataTable;
    }

    public void setDataTable(Table dataTable) {
        this.dataTable = dataTable;
    }

    public Specification(String name) {
        this.name = name;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public String getName() {
        return name;
    }

    public void addScenarios(Scenario... newScenarios) {
        for (Scenario scenario : newScenarios) {
            scenarios.add(scenario);
        }
    }

    public Scenario findScenario(String scenarioName) {
        for (Scenario scenario : scenarios) {
            if (scenario.getName().equalsIgnoreCase(scenarioName))
                return scenario;
        }
        return null;
    }

    public void addContextSteps(ScenarioStep... newContextSteps) {
        for (ScenarioStep contextStep : newContextSteps) {
            contextSteps.add(contextStep);
        }
    }

    private String getTableAsString(Table table) {
        StringBuilder tableAsString = new StringBuilder();
        tableAsString.append("|");
        for (String columnName : table.getColumnNames()) {
            tableAsString.append(columnName).append("|");
        }

        tableAsString.append("\n|");
        for (int i = 0; i < table.getColumnNames().size(); i++) {
            tableAsString.append("-------------|");
        }
        tableAsString.append("\n");
        for (List<String> row : table.getRows()) {
            tableAsString.append("|");
            for (String column : row) {
                tableAsString.append(" ").append(column).append(" ").append("|");
            }
            tableAsString.append("\n");
        }
        return tableAsString.toString();
    }


    @Override
    public String toString() {
        StringBuilder specText = new StringBuilder();
        specText.append("# ").append(name).append("\n\n");
        if(dataTable != null)
            specText.append(getTableAsString(dataTable)).append("\n");
        if(contextSteps.size() != 0){
            for (ScenarioStep contextStep : contextSteps) {
                specText.append("* ").append(contextStep.getName()).append("\n");
            }
            specText.append("\n");
        }
        if(scenarios.size() != 0){
            for (Scenario scenario : scenarios) {
                specText.append("## ").append(scenario.getName()).append("\n\n");
                for (ScenarioStep step : scenario.getSteps()) {
                    specText.append("* ").append(step.getName()).append("\n");
                    if (step.getTable() != null) {
                        specText.append(getTableAsString(step.getTable()));
                    }
                }
                specText.append("\n");
            }
        }
        specText.append("\n");
        return specText.toString();
    }

    public void saveAs(File file) throws IOException {
        Util.writeToFile(file.getAbsolutePath(), this.toString());
        this.specFile = file;
    }

    public void save() throws IOException {
        if (specFile == null) {
            throw new RuntimeException("Don't know where to save the spec to");
        }
        saveAs(specFile);
    }

    public File getSpecFile() {
        return specFile;
    }
}
