package common;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private String name;
    List<ScenarioStep> steps = new ArrayList<ScenarioStep>();

    public Scenario(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<ScenarioStep> getSteps() {
        return steps;
    }

    public void addSteps(ScenarioStep... newScenarioSteps) {
        for (ScenarioStep scenarioStep : newScenarioSteps) {
            steps.add(scenarioStep);
        }
    }

    public ScenarioStep findStep(String stepName) {
        for (ScenarioStep scenarioStep : steps) {
            if (scenarioStep.getName().equalsIgnoreCase(stepName))
                return scenarioStep;
        }
        return null;
    }

}
