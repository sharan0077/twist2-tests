package step_implementations;

import com.thoughtworks.twist2.Step;
import com.thoughtworks.twist2.Table;
import common.Scenario;
import common.ScenarioStep;
import common.Specification;

import java.util.List;

import static common.GaugeProject.currentProject;

public class SpecAndScenarioCreation {

    @Step("Create <scenario name> in <spec name> with the following steps <steps table>")
    public void addContextToSpecification(String scenarioName, String specName, Table steps) throws Exception {
        if (steps.getColumnNames().size() != 2) {
            throw new RuntimeException("Expected two columns for table");
        }

        Specification spec = currentProject.findSpecification(specName);
        if (spec == null) {
            spec = currentProject.createSpecification(specName);
        }

        Scenario scenario = new Scenario(scenarioName);
        for (List<String> rows : steps.getRows()) {
            scenario.addSteps(new ScenarioStep(rows.get(0)));
            if(rows.get(1) != null && !rows.get(1).isEmpty())
                currentProject.implementStep(rows.get(0), rows.get(1));
        }
        spec.addScenarios(scenario);
        spec.save();
    }

}
