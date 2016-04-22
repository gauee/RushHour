package pl.edu.uj.ii.webapp.execute;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pl.edu.uj.ii.DataConverter;
import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.webapp.execute.test.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by shybovycha on 22/04/16.
 */
public abstract class Task {
    protected static final Logger LOGGER = Logger.getLogger(JavaTask.class);
    protected String baseFileName;
    protected Path sourceFile;
    protected String sourceCode;

    protected abstract String getExecuteCommand();
    protected abstract String getTempFileName();

    protected String getSourceFilePath() {
        return this.sourceFile.toAbsolutePath().toString();
    }

    protected List<String> runWithInput(File inputFile) {
        ProcessBuilder processBuilder = new ProcessBuilder(this.getExecuteCommand().split("\\s+"));

        processBuilder.redirectErrorStream(true);
        processBuilder.directory(inputFile.getParentFile().getParentFile());
        processBuilder.redirectInput(inputFile);

        List<String> lines = Lists.newLinkedList();

        try {
            Process start = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream()));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            LOGGER.error("Cannot execute process.", e);
        }

        return lines;
    }

    protected void updateSourceCode(String newCode) {
        try {
            Files.write(this.sourceFile, newCode.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.printf(String.format("Can not update source code: %s", e.getMessage()));
        }
    }

    public Task processUpload(UploadFile uploadFile) throws IOException {
        this.baseFileName = uploadFile.getName().split("\\.")[0];

        Path root = Paths.get(CONFIG.getUploadedFileDir());
        this.sourceFile = Paths.get(root.toString(), this.getTempFileName());

        Files.createDirectories(this.sourceFile.getParent());

        this.sourceCode = uploadFile.getData();

        LOGGER.debug("Source code to compile:\n" + StringUtils.replaceChars(sourceCode, '\n', ' '));

        this.updateSourceCode(this.sourceCode);

        return this;
    }

    public List<List<CarMove>> getOutputFor(TestCase testCase) {
        List<String> lines = this.runWithInput(testCase.getFile());

        try {
            return DataConverter.parseOutputLines(lines);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot parse output " + lines, e);
        }
    }
}
