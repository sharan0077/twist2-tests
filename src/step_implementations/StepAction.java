package step_implementations;


import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import common.GaugeProject;
import common.ScenarioStep;
import common.StepValueExtractor;
import common.Util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StepAction {

    public static ScenarioStep createScenarioStep(String scenarioStepName) {
        ScenarioStep scenarioStep = new ScenarioStep(scenarioStepName);
        return scenarioStep;
    }

    public static void addTableToStep(ScenarioStep step, Table table) {
        step.setTable(table);
    }

    private void addImplementationAndWriteIntoJavaFile(String implementation, StringBuilder classText, String className) throws IOException {
        classText.append(") {\n").append(implementation).append("\n}");
        classText.append("}");
        Util.writeToFile(Util.combinePath(getStepImplementationsDir(), className + ".java"), classText.toString());
    }

    public void implementStepWithInlineTable(String stepText, String implementation) throws IOException {
        StepValueExtractor.StepValue stepValue = getStepValue(stepText);
        String className = getClassName();
        StringBuilder classText = new StringBuilder();
        classText.append("import com.thoughtworks.twist2.Table;\n");
        createMethodStepImplementation(classText, className, stepValue);
        getParamFormatWithTable(stepValue.paramCount, classText);
        addImplementationAndWriteIntoJavaFile(implementation, classText, className);
    }

    public void implementStep(String stepText, String implementation) throws IOException {
        StepValueExtractor.StepValue stepValue = getStepValue(stepText);
        String className = getClassName();
        StringBuilder classText = new StringBuilder();
        createMethodStepImplementation(classText, className, stepValue);
        getParamFormat(stepValue.paramCount, classText);
        addImplementationAndWriteIntoJavaFile(implementation, classText, className);
    }

    private void createMethodStepImplementation(StringBuilder classText, String className, StepValueExtractor.StepValue stepValue) {
        classText.append("import com.thoughtworks.twist2.Step;\n");
        classText.append("public class ").append(className).append("{\n");
        classText.append("@Step(\"").append(stepValue.value).append("\")\n");
        classText.append("public void ").append("stepImplementation(");
    }

    private void getParamFormatWithTable(int paramCount, StringBuilder classText) {
        for (int i = 0; i < paramCount - 1; i++) {
            classText.append("String param").append(i);
        }
        if (paramCount > 1)
            classText.append(",");
        classText.append("Table").append(" table");
    }

    private void getParamFormat(int paramCount, StringBuilder classText) {
        for (int i = 0; i < paramCount; i++) {
            classText.append("String param").append(i);
            if (i != paramCount - 1)
                classText.append(", ");
        }
    }

    private String getClassName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return String.format("Steps%d%s", System.nanoTime(), dateFormat.format(new Date()));
    }

    private StepValueExtractor.StepValue getStepValue(String stepText) {
        return new StepValueExtractor().getFor(stepText);
    }

    private String getStepImplementationsDir() {
        return new File(GaugeProject.getProjectDir(), "src/test/java").getAbsolutePath();
    }

    @Step("Add implementation <implementation> to step <step name>")
    public void implementStepWithParams(String implementation, String step) throws IOException {
        implementStep(step, implementation);
    }

    @Step("Add implementation <step implementation> to step <step> with inline table")
    public void implementStepWhichHasInlineTable(String implementation, String scenarioStepName) throws IOException {
        implementStepWithInlineTable(scenarioStepName, implementation);
    }

}
