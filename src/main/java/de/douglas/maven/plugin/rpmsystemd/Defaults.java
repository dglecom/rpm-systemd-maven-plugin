package de.douglas.maven.plugin.rpmsystemd;

public interface Defaults {

    interface Path {
        String JAVA = "/usr/bin/java";
        String RUNNABLE_JAR = "/usr/share/${project.artifactId}/${project.build.finalName}.${project.packaging}";
        String WORKING_DIRECTORY = "/var/lib/${project.artifactId}";
        String ENVIRONMENT_FILE = "/etc/sysconfig/${project.artifactId}";
    }

    String SYSTEMD_SERVICE_FILE_NAME = "${project.artifactId}.service";
    String USER = "${project.artifactId}";
    String GROUP = "${project.artifactId}";
}
