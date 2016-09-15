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

package de.douglas.maven.plugin.rpmsystemd.rpm;

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

public class PostInstMojoTest extends AbstractHarnessMojoTestCase {

    final Path postInstTestsBaseDir = TestPaths.ROOT_DIR.resolve("postinst");

    final Path postInstSrcDir = Paths.get("src/test/resources/unit/rpm-systemd-maven-plugin/postinst");

    public void testGenerateRpmPostInstFileWithDefaultSettings() throws Exception {
        String testPomName = "pom-defaults.xml";

        Path testBaseDir = createDir(postInstTestsBaseDir.resolve("defaults"));
        Path pomPath = testBaseDir.resolve(testPomName);

        Path srcPom = postInstSrcDir.resolve(testPomName);
        Files.copy(srcPom, pomPath, StandardCopyOption.REPLACE_EXISTING);

        createDir(SystemPaths$.MODULE$.generationRootDir(testBaseDir));

        PostInstMojo postInstMojo = (PostInstMojo) lookupMojo(pomPath, "generate-rpm-postinst-file");
        postInstMojo.execute();

        assertTrue(
                "Generated postinst file does not equal expected one", FileUtils.contentEquals(
                        postInstSrcDir.resolve("postinst-defaults-expected").toFile(),
                        testBaseDir.resolve("target").resolve("rpm-systemd-maven-plugin").resolve("postinst").toFile()
                )
        );
    }

    public void testGenerateRpmPostInstFileWithOverwrittenSettings() throws Exception {
        final Path testBaseDir = createDir(postInstTestsBaseDir.resolve("overwrite"));
        createDir(testBaseDir.resolve("target"));

        File pom = getTestFile(postInstSrcDir.resolve("pom-overwrite.xml").toString());

        PostInstMojo postInstMojo = (PostInstMojo) lookupMojo("generate-rpm-postinst-file", pom);
        postInstMojo.execute();

        assertTrue(
                "Generated postinst file does not equal expected one", FileUtils.contentEquals(
                        postInstSrcDir.resolve("postinst-overwrite-expected").toFile(),
                        testBaseDir.resolve("target").resolve("rpm-systemd-maven-plugin").resolve("postinst").toFile()
                )
        );
    }

    public void testGenerateRpmPostInstFileWithAdditionalDirectoriesWithoutUserAndGroup() throws Exception {
        final Path testBaseDir = createDir(postInstTestsBaseDir.resolve("additional-directories-without-user-and-group"));
        createDir(testBaseDir.resolve("target"));

        File pom = getTestFile(postInstSrcDir.resolve("pom-additional-directories-without-user-and-group.xml").toString());

        PostInstMojo postInstMojo = (PostInstMojo) lookupMojo("generate-rpm-postinst-file", pom);
        postInstMojo.execute();

        assertTrue(
                "Generated postinst file does not equal expected one", FileUtils.contentEquals(
                        postInstSrcDir.resolve("postinst-additional-directories-without-user-and-group-expected").toFile(),
                        testBaseDir.resolve("target").resolve("rpm-systemd-maven-plugin").resolve("postinst").toFile()
                )
        );
    }

    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(postInstTestsBaseDir.toFile());
    }
}
