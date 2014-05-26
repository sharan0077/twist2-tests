package step_implementations;


import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import common.Scenario;
import common.ScenarioStep;
import common.Specification;
import java.io.IOException;
import java.util.List;

import static common.GaugeProject.currentProject;

public class SpecAction {


    private Specification spec;
    private Scenario scenario;
    private ScenarioStep scenarioStep;
    private ScenarioAction scenarioAction;
    private StepAction stepAction = new StepAction();

    @Step("Create a specification <spec name> with the following contexts <steps table>")
    public void createSpecWithContexts(String specName, Table steps) throws Exception {

        if (steps.getColumnNames().size() != 2) {
            throw new RuntimeException("Expected two columns for table");
        }

        spec = currentProject.findSpecification(specName);

        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }

        for (List<String> rows : steps.getRows()) {
            spec.addContextSteps(new ScenarioStep(rows.get(0)));
            currentProject.implementStep(rows.get(0), rows.get(1));
            spec.save();
        }
    }


    @Step("Create a scenario <scenario name> in specification <spec name> with the following steps <steps table>")
    public void createSpecWithFollowingScenarioAndStepTables(String scenarioName, String specName, Table steps) throws Exception {
        spec = currentProject.findSpecification(specName);
        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }

        Scenario scenario = scenarioAction.createScenario(scenarioName);
        scenarioAction.addStepsToScenario(scenario,steps);
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Execute the spec <spec name> and ensure success")
    public void executeSpec(String specName) throws Exception {
        boolean passed;
        spec = currentProject.findSpecification(specName);
        if (spec == null) {
            throw new RuntimeException("Specified spec is not present : " + specName);
        }
        passed = currentProject.executeSpec(specName);
        System.out.println(currentProject.getStdOut());
        if (!passed) {
            System.out.println(currentProject.getStdErr());
        }
    }

    @Step("Create step <step with table> in scenario <table as parameters> in spec <Steps with table as parameters> with inline table <table>")
    public void createStepWhichTakesTableAsParameter(String stepName, String scenarioName, String specName, Table table) throws IOException {
        spec = currentProject.findSpecification(specName);
        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }
        scenario = spec.findScenario(scenarioName);
        if (scenario == null) {
            scenario = scenarioAction.createScenario(scenarioName);
        }
        ScenarioStep scenarioStep = ScenarioAction.getScenarioStep(stepName, scenario);
        StepAction.addTableToStep(scenarioStep, table);
        ScenarioAction.addStepToScenario(scenario, scenarioStep);
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Create <scenario name> in <spec name> with the following steps <steps table>")
    public void addContextToSpecification(String scenarioName, String specName, Table steps) throws Exception {
        if (steps.getColumnNames().size() != 2) {
            throw new RuntimeException("Expected two columns for table");
        }

        Specification spec = currentProject.findSpecification(specName);
        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }

        Scenario scenario = scenarioAction.createScenario(scenarioName);
        ScenarioAction.addStepsToScenario(scenario, steps);
        spec.addScenarios(scenario);
        spec.save();
    }


    @Step("Create spec <spec name> with the following dataTable <data table>")
    public void createSpecWithDataTable(String specName, Table table) throws IOException {
        spec = currentProject.findSpecification(specName);
        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }
        spec.setDataTable(table);
        spec.save();
    }

    @Step("Create step <step name> in scenario <scenario name> in spec <spec name>")
    public void createStepWithParam(String stepName, String scenarioName, String specName) throws IOException {
        spec = currentProject.findSpecification(specName);
        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }
        Scenario scenario = spec.findScenario(scenarioName);
        if (scenario == null) {
            scenario = ScenarioAction.createScenario(scenarioName);
        }
        ScenarioStep step = StepAction.createScenarioStep(stepName);
        scenarioAction.addStepToScenario(scenario, step);
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Create scenario <scenario> in spec <spec>")
    public void createSpecWithScenario(String specName, String scenarioName) throws IOException {
        spec = currentProject.findSpecification(specName);
        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }
        scenario = ScenarioAction.createScenario(scenarioName);
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Create step <step> with implementation <implementation> in scenario <scenario> in spec <spec>")
    public void createStepWithFollowingSpec(String stepName, String implementation, String scenarioName, String specName) throws IOException {
        spec = currentProject.findSpecification(specName);
        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }
        Scenario scenario = spec.findScenario(scenarioName);
        if (scenario == null) {
            scenario = ScenarioAction.createScenario(scenarioName);
        }
        ScenarioStep step = StepAction.createScenarioStep(stepName);
        stepAction.implementStep(stepName, implementation);
        scenarioAction.addStepToScenario(scenario, step);
        spec.addScenarios(scenario);
        spec.save();
    }

}
