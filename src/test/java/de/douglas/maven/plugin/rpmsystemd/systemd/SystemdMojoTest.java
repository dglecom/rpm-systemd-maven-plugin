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

import de.douglas.maven.plugin.rpmsystemd.AbstractHarnessMojoTestCase;
import de.douglas.maven.plugin.rpmsystemd.SystemPaths$;
import de.douglas.maven.plugin.rpmsystemd.TestPaths;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SystemdMojoTest extends AbstractHarnessMojoTestCase {

    final Path systemdTestsBaseDir = TestPaths.ROOT_DIR.resolve("systemd");

    final Path systemdSrcDir = Paths.get("src/test/resources/unit/rpm-systemd-maven-plugin/systemd");

    public void testGenerateSystemdFileWithDefaultSettings() throws Exception {
        String testPomName = "pom-defaults.xml";

        Path testBaseDir = createDir(systemdTestsBaseDir.resolve("defaults"));
        Path pomPath = testBaseDir.resolve(testPomName);

        Path srcPom = systemdSrcDir.resolve(testPomName);
        Files.copy(srcPom, pomPath, StandardCopyOption.REPLACE_EXISTING);

        createDir(SystemPaths$.MODULE$.generationRootDir(testBaseDir));

        SystemdMojo systemdMojo = (SystemdMojo) lookupMojo(pomPath, "generate-systemd-file");
        systemdMojo.execute();

        assertTrue(
                "Generated systemd service file does not equal expected one", FileUtils.contentEquals(
                        systemdSrcDir.resolve("test-module.service-defaults-expected").toFile(),
                        testBaseDir.resolve("target").resolve("rpm-systemd-maven-plugin").resolve("test-module.service").toFile()
                )
        );
    }

    public void testGenerateSystemdFileWithOverwrittenSettings() throws Exception {
        final Path testBaseDir = createDir(systemdTestsBaseDir.resolve("overwrite"));
        createDir(testBaseDir.resolve("target"));

        File pom = getTestFile(systemdSrcDir.resolve("pom-overwrite.xml").toString());

        SystemdMojo systemdMojo = (SystemdMojo) lookupMojo("generate-systemd-file", pom);
        systemdMojo.execute();

        assertTrue(
                "Generated systemd service file does not equal expected one", FileUtils.contentEquals(
                        systemdSrcDir.resolve("test-module.service-overwrite-expected").toFile(),
                        testBaseDir.resolve("target").resolve("rpm-systemd-maven-plugin").resolve("some.service").toFile()
                )
        );
    }

    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(systemdTestsBaseDir.toFile());
    }
}
