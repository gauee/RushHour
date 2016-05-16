package pl.edu.uj.ii.webapp.solution;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 5/16/16.
 */
public class Source {
    private static final String OUTPUTS = "outputs";
    private static final Logger LOGGER = Logger.getLogger(Source.class);
    private final Map<String, Pair<Integer, Integer>> tasksInProgress = Maps.newHashMap();

    public void startProcessing(String solutionId, int testCasesAmount) {
        tasksInProgress.put(solutionId, new MutablePair<>(testCasesAmount, 0));
    }

    public String getExecutionStatus(String solutionId) {
        Pair<Integer, Integer> taskProgress = tasksInProgress.get(solutionId);
        if (taskProgress == null) {
            return "Solution executed with all test cases.";
        }
        return "Executing solution in progress. Executed " + taskProgress.getValue() + " of " + taskProgress.getKey();
    }

    public void save(Solution solution) {
        String solutionId = solution.getId();
        Path outputDir = getSolutionsDir(solutionId);
        createNonExistingDir(outputDir);
        updateTasksInProgressStatus(solutionId);
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
        Path outputDir = getSolutionsDir(solutionId);
        List<Solution> solutions = Lists.newArrayList();
        try (Stream<Path> fileStream = Files.list(outputDir)) {
            fileStream
                    .map(this::readSolution)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(() -> solutions));
        } catch (IOException e) {
            LOGGER.error("Cannot list files from dir " + solutionId);
        }
        return solutions;
    }

    private void updateTasksInProgressStatus(String solutionId) {
        if (tasksInProgress.containsKey(solutionId)) {
            Pair<Integer, Integer> taskProgress = tasksInProgress.get(solutionId);
            if (taskProgress.getValue() + 1 >= taskProgress.getKey()) {
                tasksInProgress.remove(solutionId);
            } else {
                taskProgress.setValue(taskProgress.getValue() + 1);
            }
        }
    }

    private Path getSolutionsDir(String solutionId) {
        return Paths.get(CONFIG.getUploadedFileDir(), OUTPUTS, solutionId);
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
