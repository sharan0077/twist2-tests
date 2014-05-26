package step_implementations;


import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import common.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpecAction {

    private StepAction stepAction = new StepAction();
    private static GaugeProject currentProject = GaugeProject.getCurrentProject();
    private static List<Specification> allSpecifications = new ArrayList<Specification>();

    public static Specification createSpecification(String name) throws IOException {
        File specFile = new File(currentProject.getProjectDir(), Util.combinePath(GaugeProject.SPECS_DIR_NAME, name) + ".spec");
        if (specFile.exists()) {
            throw new RuntimeException("Failed to create specification with name: " + name + "libs" + specFile.getAbsolutePath() + ": File already exists");
        }
        Specification specification = new Specification(name);
        specification.saveAs(specFile);
        allSpecifications.add(specification);
        return specification;
    }

    public Specification findSpecification(String specName) {
        for (Specification specification : allSpecifications) {
            if (specification.getName().equalsIgnoreCase(specName)) {
                return specification;
            }
        }

        return null;
    }


    @Step("Create a specification <spec name> with the following contexts <steps table>")
    public void createSpecWithContexts(String specName, Table steps) throws Exception {

        if (steps.getColumnNames().size() != 2) {
            throw new RuntimeException("Expected two columns for table");
        }

        Specification spec = findSpecification(specName);

        if (spec == null) {
            spec = createSpecification(specName);
        }

        for (List<String> rows : steps.getRows()) {
            spec.addContextSteps(new ScenarioStep(rows.get(0)));
            StepAction.implementStep(rows.get(0), rows.get(1));
            spec.save();
        }
    }


    @Step("Create a scenario <scenario name> in specification <spec name> with the following steps <steps table>")
    public void createSpecWithFollowingScenarioAndStepTables(String scenarioName, String specName, Table steps) throws Exception {
        Specification spec = findSpecification(specName);
        if (spec == null) {
            spec = createSpecification(specName);
        }

        Scenario scenario = ScenarioAction.createScenario(scenarioName);
        ScenarioAction.addStepsToScenario(scenario, steps);
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Execute the spec <spec name> and ensure success")
    public void executeSpec(String specName) throws Exception {
        boolean passed;
        Specification spec = findSpecification(specName);
        if (spec == null) {
            throw new RuntimeException("Specified spec is not present : " + specName);
        }
        passed = currentProject.executeGaugeSpec(specName);
        System.out.println(currentProject.getStdOut());
        if (!passed) {
            System.out.println(currentProject.getStdErr());
        }
    }

    @Step("Create step <step with table> in scenario <table as parameters> in spec <Steps with table as parameters> with inline table <table>")
    public void createStepWhichTakesTableAsParameter(String stepName, String scenarioName, String specName, Table table) throws IOException {
        Specification spec = findSpecification(specName);
        if (spec == null) {
            spec = createSpecification(specName);
        }
        Scenario scenario = spec.findScenario(scenarioName);
        if (scenario == null) {
            scenario = ScenarioAction.createScenario(scenarioName);
        }
        ScenarioStep scenarioStep = ScenarioAction.getScenarioStep(stepName, scenario);
        StepAction.addTableToStep(scenarioStep, table);
        ScenarioAction.addStepToScenario(scenario, scenarioStep);
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Create <scenario name> in <spec name> with the following steps <steps table>")
    public void addContextToSpecification(String scenarioName, String specName, Table stepsWithImpl) throws Exception {
        if (stepsWithImpl.getColumnNames().size() != 2) {
            throw new RuntimeException("Expected two columns for table");
        }

        Specification spec = findSpecification(specName);
        if (spec == null) {
            spec = createSpecification(specName);
        }

        Scenario scenario = ScenarioAction.createScenario(scenarioName);
        ScenarioAction.addStepsToScenario(scenario, stepsWithImpl);
        spec.addScenarios(scenario);
        spec.save();
    }


    @Step("Create spec <spec name> with the following dataTable <data table>")
    public void createSpecWithDataTable(String specName, Table table) throws IOException {
        Specification spec = findSpecification(specName);
        if (spec == null) {
            spec = createSpecification(specName);
        }
        spec.setDataTable(table);
        spec.save();
    }

    @Step("Create step <step name> in scenario <scenario name> in spec <spec name>")
    public void createStepInScenarioAndSpec(String stepName, String scenarioName, String specName) throws IOException {
        Specification spec = findSpecification(specName);
        if (spec == null) {
            spec = createSpecification(specName);
        }
        Scenario scenario = spec.findScenario(scenarioName);
        if (scenario == null) {
            scenario = ScenarioAction.createScenario(scenarioName);
        }
        ScenarioStep step = StepAction.createScenarioStep(stepName);
        ScenarioAction.addStepToScenario(scenario, step);
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Create scenario <scenario> in spec <spec>")
    public void createSpecWithScenario(String specName, String scenarioName) throws IOException {
        Specification spec = findSpecification(specName);
        if (spec == null) {
            spec = createSpecification(specName);
        }
        Scenario scenario = ScenarioAction.createScenario(scenarioName);
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Create step <step> with implementation <implementation> in scenario <scenario> in spec <spec>")
    public void createStepWithFollowingSpec(String stepName, String implementation, String scenarioName, String specName) throws IOException {
        Specification spec = findSpecification(specName);
        if (spec == null) {
            spec = createSpecification(specName);
        }
        Scenario scenario = spec.findScenario(scenarioName);
        if (scenario == null) {
            scenario = ScenarioAction.createScenario(scenarioName);
        }
        ScenarioStep step = StepAction.createScenarioStep(stepName);
        stepAction.implementStep(stepName, implementation);
        ScenarioAction.addStepToScenario(scenario, step);
        spec.addScenarios(scenario);
        spec.save();
    }

}
