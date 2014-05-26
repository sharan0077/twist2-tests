package step_implementations;


import com.thoughtworks.gauge.Table;
import common.Scenario;
import common.ScenarioStep;

import java.io.IOException;
import java.util.List;

import static common.GaugeProject.currentProject;

public class ScenarioAction {

    public static Scenario createScenario(String scenarioName) {
        Scenario scenario = new Scenario(scenarioName);
        return scenario;
    }

    public static void addStepsToScenario(Scenario scenario, Table steps) throws IOException {
        for (List<String> rows : steps.getRows()) {
            scenario.addSteps(new ScenarioStep(rows.get(0)));
            if (rows.get(1) != null && !rows.get(1).isEmpty())
                currentProject.implementStep(rows.get(0), rows.get(1));
        }
    }

    public static ScenarioStep getScenarioStep(String stepName, Scenario scenario) {
        ScenarioStep scenarioStep = scenario.findStep(stepName);
        if (scenarioStep == null) {
            scenarioStep = StepAction.createScenarioStep(stepName);
        }
        return scenarioStep;
    }

    public static void addStepToScenario(Scenario scenario, ScenarioStep scenarioStep) {
        scenario.addSteps(scenarioStep);
    }

}
