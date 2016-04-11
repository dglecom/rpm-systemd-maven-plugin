package de.douglas.maven.plugin.rpmsystemd;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface TestPaths {

    Path ROOT_DIR = Paths.get("target/test-harness/rpm-systemd-maven-plugin");
}
