package pl.edu.uj.ii.webapp.solution;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 5/16/16.
 */
public class Source {
    private static final String OUTPUTS = "outputs";
    private static final Logger LOGGER = Logger.getLogger(Source.class);
    public static final String EXECUTION_ERROR_INFO = "executionError.info";
    private final Map<String, Pair<Integer, Integer>> tasksInProgress = Maps.newHashMap();

    public void startProcessing(String solutionId, int testCasesAmount) {
        tasksInProgress.put(solutionId, new MutablePair<>(testCasesAmount, 0));
        createNonExistingDir(getSolutionsDir(solutionId));
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
        updateTasksInProgressStatus(solutionId);
        try {
            if (isEmpty(solution.getTestCaseId())) {
                LOGGER.warn("No solution will be save for time outed one.");
                return;
            }
            Path outputFile = Paths.get(getSolutionsDir(solutionId).toString(), solution.getTestCaseId());
            try (FileOutputStream outStream = new FileOutputStream(outputFile.toFile())) {
                IOUtils.writeLines(solution.getMoves(), "\n", outStream);
            }
        } catch (IOException e) {
            LOGGER.warn("Cannot create file " + solution.getTestCaseId());
        }
    }

    public void save(String errorMessage, String solutionId) {
        Path outputFile = Paths.get(getSolutionsDir(solutionId).toString(), EXECUTION_ERROR_INFO);
        try (FileOutputStream outStream = new FileOutputStream(outputFile.toFile())) {
            IOUtils.write(errorMessage, outStream);
        } catch (IOException e) {
            LOGGER.error("Cannot create errorMessage.");
        }
    }

    public List<Solution> load(String solutionId) {
        Path outputDir = getSolutionsDir(solutionId);
        List<Solution> solutions = Lists.newArrayList();
        File[] files = outputDir.toFile().listFiles();
        if (files == null) {
            return solutions;
        }
        for (File file : files) {
            Solution solution = readSolution(file);
            if (solution != null) {
                solutions.add(solution);
            }
        }
        return solutions;
    }

    public String loadError(String solutionId) {
        Path errorFile = Paths.get(getSolutionsDir(solutionId).toString(), EXECUTION_ERROR_INFO);
        if (Files.exists(errorFile)) {
            try (InputStream inStream = new FileInputStream(errorFile.toFile())) {
                return String.join("", IOUtils.readLines(inStream));
            } catch (IOException e) {
                LOGGER.error("Cannot load file " + errorFile.toFile().toString());
            }
        }
        return null;
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

    private Solution readSolution(File file) {
        try (InputStream inStream = new FileInputStream(file)) {
            return new Solution(
                    "",
                    file.getName(),
                    IOUtils.readLines(inStream)
            );
        } catch (IOException e) {
            LOGGER.error("Cannot load file " + file.toString());
        }
        return null;
    }


}
