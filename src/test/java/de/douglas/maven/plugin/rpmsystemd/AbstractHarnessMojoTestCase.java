package de.douglas.maven.plugin.rpmsystemd;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.eclipse.aether.DefaultRepositorySystemSession;

import java.io.File;
import java.nio.file.Path;

public abstract class AbstractHarnessMojoTestCase extends AbstractMojoTestCase {

    protected Path createDir(Path path) {
        path.toFile().mkdirs();

        return path;
    }

    protected Mojo lookupMojo(Path pomPath, String goal) throws Exception {
        File pom = getTestFile(pomPath.toString());
        assertTrue(pom.exists());

        MavenExecutionRequest executionRequest = new DefaultMavenExecutionRequest();
        ProjectBuildingRequest buildingRequest = executionRequest.getProjectBuildingRequest();
        buildingRequest.setRepositorySession(new DefaultRepositorySystemSession());

        ProjectBuilder projectBuilder = lookup(ProjectBuilder.class);
        MavenProject project = projectBuilder.build(pom, buildingRequest).getProject();

        return lookupConfiguredMojo(project, goal);
    }
}
