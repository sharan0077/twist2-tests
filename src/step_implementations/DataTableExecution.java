package step_implementations;

import com.thoughtworks.twist2.Step;
import com.thoughtworks.twist2.Table;
import common.GaugeProject;
import common.Scenario;
import common.ScenarioStep;
import common.Specification;

import java.io.IOException;

public class DataTableExecution {

    private final GaugeProject currentProject;
    private Specification spec;
    private Scenario scenario;
    private ScenarioStep scenarioStep;

    public DataTableExecution() {
        currentProject = GaugeProject.getCurrentProject();
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
        scenario = new Scenario(scenarioName);
        scenario.addSteps(new ScenarioStep(stepName));
        spec.addScenarios(scenario);
        spec.save();
    }

    @Step("Add implementation <implementation> to step <step name>")
    public void implementStepWithParams(String implementation, String step) throws IOException {
        scenarioStep = scenario.findStep(step);
        currentProject.implementStep(step, implementation);
        spec.save();
    }

}
