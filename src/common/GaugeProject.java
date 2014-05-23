package common;


import com.thoughtworks.gauge.Table;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GaugeProject {

    public static final String PRODUCT_ROOT = "GAUGE_ROOT";
    public static final String PRODUCT_PREFIX = "GAUGE_";
    private static String executableName = "twist2";
    private static String specsDirName = "specs";
    private static String conceptsDirName = "concepts";
    private static String stepImplementationsDir = "src/test/java";
    private ArrayList<Concept> concepts = new ArrayList<Concept>();

    private static File projectDir;
    private String language;
    private Process lastProcess = null;
    private ArrayList<Specification> specifications = new ArrayList<Specification>();
    public static GaugeProject currentProject;
    private String lastProcessStderr;
    private String lastProcessStdout;

    public static GaugeProject getCurrentProject() {
        if (currentProject == null) {
            throw new RuntimeException("Gauge project is not initialized yet");
        }
        return currentProject;
    }


    public void addConcepts(Concept... newConcepts) {
        for (Concept concept : newConcepts) {
            concepts.add(concept);
        }
    }

    public GaugeProject(File projectDir, String language) {
        this.projectDir = projectDir;
        this.language = language;
        currentProject = this;
    }

    public boolean initialize() throws Exception {
        executeGaugeCommand("--init", language);
        System.out.println(lastProcessStdout);
        System.out.println(lastProcessStderr);
        return lastProcess.exitValue() == 0;
    }

    public String getStdOut() throws IOException {
        return lastProcessStdout;
    }

    public String getStdErr() throws IOException {
        return lastProcessStderr;
    }

    public static File getProjectDir() {
        return projectDir;
    }

    public Specification createSpecification(String name) throws IOException {
        File specFile = new File(projectDir, Util.combinePath(specsDirName, name) + ".spec");
        if (specFile.exists()) {
            throw new RuntimeException("Failed to create specification with name: " + name + "." + specFile.getAbsolutePath() + ": File already exists");
        }
        Specification specification = new Specification(name);
        specification.saveAs(specFile);
        specifications.add(specification);
        return specification;
    }

    public Specification findSpecification(String specName) {
        for (Specification specification : specifications) {
            if (specification.getName().equalsIgnoreCase(specName)) {
                return specification;
            }
        }

        return null;
    }


    public Concept createConcept(String name, Table steps) throws IOException {

        String specDirPath = new File(projectDir, specsDirName).getAbsolutePath();
        File conceptsDir = new File(specDirPath, conceptsDirName);
        if (!conceptsDir.exists()) {
            conceptsDir.mkdir();
        }
        File conceptFile = new File(conceptsDir, name + ".cpt");
        if (conceptFile.exists()) {
            throw new RuntimeException("Failed to create concept: " + name + "." + conceptFile.getAbsolutePath() + " : File already exists");
        }
        Concept concept = new Concept(name);
        for (List<String> row : steps.getRows()) {
            System.out.println(row.get(0));
            concept.addSteps(row.get(0));
            implementStep(row.get(0), row.get(1));
        }
        concept.saveAs(conceptFile);
        concepts.add(concept);
        return concept;
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
        return new File(getProjectDir(), "src/test/java").getAbsolutePath();
    }

    public boolean execute() throws Exception {
        executeGaugeCommand("specs/");
        return lastProcess.exitValue() == 0;
    }

    public boolean executeSpec(String specName) throws Exception {
        executeGaugeCommand("specs" + File.separator + specName + ".spec");
        return lastProcess.exitValue() == 0;
    }

    public boolean executeGaugeCommand(String... args) throws IOException, InterruptedException {
        ArrayList<String> command = new ArrayList<String>();
        command.add(executableName);
        for (String arg : args) {
            command.add(arg);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(command.toArray(new String[command.size()]));
        processBuilder.directory(projectDir);
        filterConflictingEnv(processBuilder);
        lastProcess = processBuilder.start();
        lastProcess.waitFor();
        lastProcessStdout = IOUtils.toString(lastProcess.getInputStream());
        lastProcessStderr = IOUtils.toString(lastProcess.getErrorStream());
        return lastProcess.exitValue() == 0;
    }

    private void filterConflictingEnv(ProcessBuilder processBuilder) {
        for (String env : processBuilder.environment().keySet()) {
            if (!env.toUpperCase().equals(PRODUCT_ROOT) && env.toUpperCase().contains(PRODUCT_PREFIX)) {
                processBuilder.environment().put(env, "");
            }
        }
    }

}
