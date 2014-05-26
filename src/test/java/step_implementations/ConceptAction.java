package step_implementations;

import com.thoughtworks.gauge.Table;
import common.Concept;
import common.GaugeProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConceptAction {

    static List<Concept> concepts = new ArrayList<Concept>();

    public static Concept createConcept(String name, Table steps) throws IOException {

        String specDirPath = new File(GaugeProject.getProjectDir(), GaugeProject.SPECS_DIR_NAME).getAbsolutePath();
        File conceptsDir = new File(specDirPath, GaugeProject.CONCEPTS_DIR_NAME);
        if (!conceptsDir.exists()) {
            conceptsDir.mkdir();
        }
        File conceptFile = new File(conceptsDir, name + ".cpt");
        if (conceptFile.exists()) {
            throw new RuntimeException("Failed to create concept: " + name + "libs" + conceptFile.getAbsolutePath() + " : File already exists");
        }
        Concept concept = new Concept(name);
        for (List<String> row : steps.getRows()) {
            System.out.println(row.get(0));
            concept.addSteps(row.get(0));
            StepAction.implementStep(row.get(0), row.get(1));
        }
        concept.saveAs(conceptFile);
        concepts.add(concept);
        return concept;
    }

    public static void addConcepts(Concept... newConcepts) {
        for (Concept concept : newConcepts) {
            concepts.add(concept);
        }
    }

    public List<Concept> getConcepts() {
        return concepts;
    }
}
