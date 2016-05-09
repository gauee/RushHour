package pl.edu.uj.ii.webapp.execute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class JavaTask extends CompilableTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaTask.class);
    private final String jdkDir;

    public JavaTask(String jdkDir, String compiledFileDir) {
        super(compiledFileDir);
        this.jdkDir = jdkDir;
    }

    protected String getTempFileName() {
        return String.format("%s.java", baseFileName);
    }

    @Override
    public void compile() throws IOException, ClassNotFoundException {
        String filePackage = getSolutionDir().substring(0, getSolutionDir().length() - 2).replaceAll("\\" + File.separator, ".");
        String newCode = changePackageInsideSolution(filePackage);
        this.updateSourceCode(newCode);
        String sourceFile = this.sourceFile.toFile().getAbsolutePath();
        ProcessBuilder processBuilder = createProcessBuilder(this.jdkDir + "bin/javac", sourceFile);
        StringBuilder compilerOut = new StringBuilder();
        try {
            Process start = processBuilder.start();
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(start.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(start.getErrorStream()));
            String line;
            compilerOut.append("Errors:");
            while ((line = errorReader.readLine()) != null) {
                compilerOut.append(line);
            }
            compilerOut.append(" Compiler output:");
            while ((line = outputReader.readLine()) != null) {
                compilerOut.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("Cannot execute process.", e);
        }
        LOGGER.info("Compilation finished. " + compilerOut.toString());
    }

    private String changePackageInsideSolution(String filePackage) {
        return this.sourceCode.replaceFirst("package\\s+.*?;", String.format("package %s;", filePackage));
    }

    @Override
    ProcessBuilder createExecutionProcess() {
        ProcessBuilder processBuilder = createProcessBuilder("../" + jdkDir + "bin/java", getSolutionDir() + baseFileName);
        processBuilder.directory(new File(CONFIG.getUploadedFileDir()));
        return processBuilder;
    }

}
