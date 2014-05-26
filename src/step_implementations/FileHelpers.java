package step_implementations;


import com.thoughtworks.gauge.Step;
import common.GaugeProject;
import common.Util;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileHelpers {

    File projectDir;

    public FileHelpers() {
        projectDir = GaugeProject.getProjectDir();
    }

    @Step("Replicate file <file>")
    public void copyFile(String filePath) throws IOException {
        File destFile = new File(Util.combinePath(projectDir.getAbsolutePath(), filePath));
        File fileToCopy = new File(filePath);
        FileUtils.copyFile(fileToCopy, destFile);
    }
}