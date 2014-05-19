package step_implementations;


import com.thoughtworks.twist2.Step;
import com.thoughtworks.twist2.Table;
import common.GaugeProject;
import common.Scenario;
import common.ScenarioStep;
import common.Specification;

import java.io.IOException;

public class InLineTableAsParameter {

    private final GaugeProject currentProject;
    private Specification spec;
    private Scenario scenario;
    private ScenarioStep scenarioStep;

    public InLineTableAsParameter() {
        currentProject = GaugeProject.getCurrentProject();
    }

    @Step("Create step <step with table> in scenario <table as parameters> in spec <Steps with table as parameters> with inline table <table>")
    public void createStepWhichTakesTableAsParameter(String stepName, String scenarioName, String specName, Table table) throws IOException {
        spec = currentProject.findSpecification(specName);
        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }
        scenario = spec.findScenario(scenarioName);
        if (scenario == null) {
            scenario = new Scenario(scenarioName);
        }
        scenarioStep = scenario.findStep(stepName);
        if (scenarioStep == null) {
            scenarioStep = new ScenarioStep(stepName);
            scenarioStep.setTable(table);
        }
        scenario.addSteps(scenarioStep);
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Add implementation <step implementation> to step <step> with inline table")
    public void implementStepWithInlineTable(String implementation, String scenarioStepName) throws IOException {
        currentProject.implementStepWithInlineTable(scenarioStepName, implementation);
        spec.save();
    }
}
