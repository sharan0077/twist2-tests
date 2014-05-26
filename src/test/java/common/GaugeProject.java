package common;


import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GaugeProject {

    public static final String PRODUCT_ROOT = "GAUGE_ROOT";
    public static final String PRODUCT_PREFIX = "GAUGE_";
    public static final String EXECUTABLE_NAME = "gauge";
    public static final String SPECS_DIR_NAME = "specs";
    public static final String CONCEPTS_DIR_NAME = "concepts";
    public static final String STEP_IMPLEMENTATIONS_DIR = "src/test/java";
    private ArrayList<Concept> concepts = new ArrayList<Concept>();

    private static File projectDir;
    private static String language;
    private static Process lastProcess = null;
    private ArrayList<Specification> specifications = new ArrayList<Specification>();
    public static GaugeProject currentProject;
    private static String lastProcessStderr;
    private static String lastProcessStdout;

    public static GaugeProject getCurrentProject() {
        if (currentProject == null) {
            throw new RuntimeException("Gauge project is not initialized yet");
        }
        return currentProject;
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



    public boolean execute() throws Exception {
        executeGaugeCommand("specs/");
        return lastProcess.exitValue() == 0;
    }

    public boolean executeGaugeSpec(String specName) throws Exception {
        executeGaugeCommand("specs" + File.separator + specName + ".spec");
        return lastProcess.exitValue() == 0;
    }

    public boolean executeGaugeCommand(String... args) throws IOException, InterruptedException {

        ArrayList<String> command = new ArrayList<String>();
        command.add(EXECUTABLE_NAME);
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
