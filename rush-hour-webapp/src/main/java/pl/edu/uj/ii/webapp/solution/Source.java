package pl.edu.uj.ii.webapp.solution;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 5/16/16.
 */
public class Source {
    private static final Logger LOGGER = Logger.getLogger(Source.class);

    public void save(Solution solution) {
        Path outputDir = Paths.get(CONFIG.getUploadedFileDir(), solution.getId());
        createNonExistingDir(outputDir);
        try {
            Path outputFile = Paths.get(outputDir.toString(), solution.getTestCaseId());
            try (FileOutputStream outStream = new FileOutputStream(outputFile.toFile())) {
                IOUtils.writeLines(solution.getMoves(), "\n", outStream);
            }
        } catch (IOException e) {
            LOGGER.warn("Cannot create file " + solution.getTestCaseId());
        }

    }

    public List<Solution> load(String solutionId) {
        Path outputDir = Paths.get(CONFIG.getUploadedFileDir(), solutionId);
        List<Solution> solutions = Lists.newArrayList();
        try {
            Files.list(outputDir)
                    .map(this::readSolution)
                    .filter(Objects::nonNull)
                    .peek(solution -> solutions.add(solution))
                    .close();
        } catch (IOException e) {
            LOGGER.error("Cannot list files from dir " + solutionId);
        }

        return solutions;
    }

    private void createNonExistingDir(Path outputDir) {
        if (Files.notExists(outputDir)) {
            LOGGER.info("Solution dir " + outputDir + " does not exist, start creating.");
            try {
                Files.createDirectories(outputDir);
            } catch (IOException e) {
                LOGGER.error("Cannot create dir " + outputDir, e);
            }
        }
    }

    private Solution readSolution(Path path) {
        try (InputStream inStream = new FileInputStream(path.toFile())) {
            return new Solution(
                    "",
                    path.toFile().getName(),
                    IOUtils.readLines(inStream)
            );
        } catch (IOException e) {
            LOGGER.error("Cannot load file " + path.toString());
        }
        return null;
    }

}
