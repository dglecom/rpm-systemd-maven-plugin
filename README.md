# RPM Systemd Maven Plugin

This plugin can be used to generate files for RPM packages and systemd
services.

[![Build Status](https://travis-ci.org/dglecom/rpm-systemd-maven-plugin.svg?branch=master)](https://travis-ci.org/dglecom/rpm-systemd-maven-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/de.douglas.maven.plugin/rpm-systemd-maven-plugin.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22de.douglas.maven.plugin%22%20AND%20a%3A%22rpm-systemd-maven-plugin%22)

## Usage

The plugin currently supports two goals:

* **generate-systemd-file**: This generates a systemd file for the module
* **generate-rpm-postinst-file**: This generates a postinst file to set up
    the target system

```xml
<plugin>
    <groupId>de.douglas.maven.plugin</groupId>
    <artifactId>rpm-systemd-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <phase>generate-resources</phase>
            <goals>
                <goal>generate-systemd-file</goal>
                <goal>generate-rpm-install-files</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Usage in conjunction with the RPM Maven Plugin

This plugin can be used in conjunction with the
[RPM Maven Plugin](http://www.mojohaus.org/rpm-maven-plugin/) to create
a RPM package including the systemd and postinst file.

```xml
<plugins>
    <plugin>
        <groupId>de.douglas.maven.plugin</groupId>
        <artifactId>rpm-systemd-maven-plugin</artifactId>
        <executions>
            <execution>
                <phase>generate-resources</phase>
                <goals>
                    <goal>generate-systemd-file</goal>
                    <goal>generate-rpm-postinst-file</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>rpm-maven-plugin</artifactId>
        <configuration>
            <mappings>
                <mapping>
                    <directory>/usr/lib/systemd/system</directory>
                    <sources>
                        <source>
                            <location>${project.build.directory}/rpm-systemd-maven-plugin/${project.artifactId}.service</location>
                        </source>
                    </sources>
                    <directoryIncluded>false</directoryIncluded>
                    <configuration>false</configuration>
                </mapping>
            </mappings>
            <postinstallScriptlet>
                <scriptFile>target/rpm-systemd-maven-plugin/postinst</scriptFile>
                <fileEncoding>utf-8</fileEncoding>
            </postinstallScriptlet>
            ...
        </configuration>
    </plugin>
</plugins>
```

### Override variables

You can override the following variables in the plugin configuration:

#### javaPath
* Path to the java binary in the systemd service file
* Defaults to **/usr/bin/java**

#### runnableJarPath
* Path to the runnable jar of the module in the systemd service file
* Defaults to **/usr/share/${project.artifactId}/${project.build.finalName}.${project.packaging}**

#### workingDirectoryPath
* Path to the working directory in the systemd service file
* Used as home directory for the user the module will be executed with
* Defaults to **/var/lib/${project.artifactId}**

#### environmentFilePath
* Path to the environment file in the systemd service file
* Created in the postinst file
* Defaults to **/etc/sysconfig/${project.artifactId}**

#### systemdServiceFileName
* Name of the systemd service file
* Used in the postinst file to preset the systemd service
* Defaults to **${project.artifactId}.service**

#### user
* User to be used for module execution in the systemd service file
* Used to set permissions for directories
* Defaults to **${project.artifactId}**

#### group
* Group to be used for module execution in the systemd service file
* Used to set permissions for directories
* Defaults to **${project.artifactId}**

#### additionalDirectories
* List of additional directories to be created in the postinst file
* Defaults to an empty list

#### Example

To override the **runnableJarPath** and add **additional directories**, use

```xml
<plugins>
    <plugin>
        <groupId>de.douglas.maven.plugin</groupId>
        <artifactId>rpm-systemd-maven-plugin</artifactId>
        <configuration>
            <runnableJarPath>/opt/${project.artifactId}/${project.build.finalName}-jarlotte.jar</runnableJarPath>
            <additionalDirectories>
                <additionalDirectory>
                    <directory>/etc/${project.artifactId}</directory>
                    <user>root</user>
                    <group>root</group>
                </additionalDirectory>
                <additionalDirectory>
                    <directory>/var/log/${project.artifactId}</directory>
                    <user>${project.artifactId}</user>
                    <group>${project.artifactId}</group>
                </additionalDirectory>
            </additionalDirectories>
        </configuration>
        ...
    </plugin>
</plugins>
```

or

```xml
<properties>
    <runnableJarPath>/opt/${project.artifactId}/${project.build.finalName}-jarlotte.jar</runnableJarPath>
</properties>
```

## License
The RPM Systemd Maven Plugin is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).
