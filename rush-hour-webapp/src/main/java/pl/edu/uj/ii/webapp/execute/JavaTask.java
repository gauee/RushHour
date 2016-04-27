package pl.edu.uj.ii.webapp.execute;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gauee on 4/7/16.
 */
public class JavaTask extends CompilableTask {
    private final String compiledFileDir;
    private final String jdkDir;
    private String compiledFilePath;
    private String packageDir;

    public JavaTask(String compiledFileDir, String jdkDir) {
        this.compiledFileDir = compiledFileDir;
        this.jdkDir = jdkDir;
        this.packageDir = generateNewPackagePath();
    }

    protected String getTempFileName() {
        return String.format("%s/%s.java", packageDir, baseFileName);
    }

    @Override
    public Task compile() throws IOException, ClassNotFoundException {
        String className = this.baseFileName.split("\\.")[0];

        String filePackage = this.packageDir.replaceAll("\\" + File.separator, ".");

        // patch package name in the source code
        String newCode = this.sourceCode.replaceFirst("package\\s+.*?;", String.format("package %s;", filePackage));
        this.updateSourceCode(newCode);

        ProcessBuilder processBuilder = new ProcessBuilder(this.getCompileCommand(), this.getSourceFilePath());
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(this.sourceFile.getParent().getParent().getParent().toFile());

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

            compilerOut.append("\nCompiler output:");

            while ((line = outputReader.readLine()) != null) {
                compilerOut.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("Cannot execute process.", e);
        }

        LOGGER.debug("Compilation finished.");
        LOGGER.info(compilerOut.toString());

        this.compiledFilePath = filePackage + "." + className;

        return this;
    }

    protected String getExecuteCommand() {
        return String.format("%s/bin/java %s", this.jdkDir, this.compiledFilePath);
    }

    protected String getCompileCommand() {
        return String.format("%s/bin/javac", this.jdkDir, this.compiledFilePath);
    }

    private String generateNewPackagePath() {
        return String.format("runtimeCompiled/%s/_%d", this.compiledFileDir, System.currentTimeMillis());
    }
}
