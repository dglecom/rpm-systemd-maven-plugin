/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

package de.douglas.maven.plugin.rpmsystemd.systemd;

import de.douglas.maven.plugin.rpmsystemd.Defaults;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.nio.file.Path;

@Mojo(name = "generate-systemd-file")
public class SystemdMojo extends AbstractMojo {

    @Parameter(property = "basedir", required = true)
    private File baseDirectory;

    @Parameter(property = "project.description", required = true)
    private String description;

    @Parameter(property = "javaPath", defaultValue = Defaults.Path.JAVA, required = true)
    private String javaPath;

    @Parameter(property = "runnableJarPath", defaultValue = Defaults.Path.RUNNABLE_JAR, required = true)
    private String runnableJarPath;

    @Parameter(property = "workingDirectoryPath", defaultValue = Defaults.Path.WORKING_DIRECTORY, required = true)
    private String workingDirectoryPath;

    @Parameter(property = "environmentFilePath", defaultValue = Defaults.Path.ENVIRONMENT_FILE, required = true)
    private String environmentFilePath;

    @Parameter(property = "systemdServiceFileName", defaultValue = Defaults.SYSTEMD_SERVICE_FILE_NAME, required = true)
    private String systemdServiceFileName;

    @Parameter(property = "user", defaultValue = Defaults.USER, required = true)
    private String user;

    @Parameter(property = "group", defaultValue = Defaults.GROUP, required = true)
    private String group;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Path systemdFile = new SystemdFileGenerator().generateSystemdServiceFile(baseDirectory, workingDirectoryPath, environmentFilePath, systemdServiceFileName, runnableJarPath,
                javaPath, description, user, group);
        getLog().info("Wrote systemd file to " + systemdFile.toString());
    }
}
