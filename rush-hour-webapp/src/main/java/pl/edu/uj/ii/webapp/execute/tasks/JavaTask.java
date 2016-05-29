package pl.edu.uj.ii.webapp.execute.tasks;

import java.io.File;
import java.io.IOException;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class JavaTask extends CompilableTask {
    private final String jdkDir;

    public JavaTask(String jdkDir, String compiledFileDir) {
        super(compiledFileDir);
        this.jdkDir = jdkDir;
    }

    protected String getTempFileName() {
        return String.format("%s.java", baseFileName);
    }

    @Override
    void preCompile() throws IOException, ClassNotFoundException {
        String filePackage = getUniqueSolutionDir().substring(0, getUniqueSolutionDir().length() - 1).replaceAll("\\" + File.separator, ".");
        String newCode = changePackageInsideSolution(filePackage);
        this.updateSourceCode(newCode);
    }

    @Override
    String createCompileCommand() {
        return this.jdkDir + "bin/javac";
    }

    private String changePackageInsideSolution(String filePackage) {
        return this.sourceCode.replaceFirst("package\\s+.*?;", String.format("package %s;", filePackage));
    }

    @Override
    ProcessBuilder createExecutionProcess() {
        ProcessBuilder processBuilder = createProcessBuilder("../" + jdkDir + "bin/java", getUniqueSolutionDir() + baseFileName);
        processBuilder.directory(new File(CONFIG.getUploadedFileDir()));
        return processBuilder;
    }

}
