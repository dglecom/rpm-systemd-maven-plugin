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

package de.douglas.maven.plugin.rpmsystemd.systemd

import java.io.File
import java.nio.file.Path

import de.douglas.maven.plugin.rpmsystemd.{FileSupport, SystemPaths}

class SystemdFileGenerator {

  def generateSystemdServiceFile(baseDir: File, workingDirectory: String, environmentFile: String, systemdServiceFileName: String, runnableJarPath: String, javaPath: String, description: String, user: String, group: String): Path = {
    val generatedResourcesDir = SystemPaths.generationRootDir(baseDir.toPath)
    generatedResourcesDir.toFile.mkdir()

    val systemdServiceFile = generatedResourcesDir.resolve(systemdServiceFileName)

    val systemdServiceContents = contents(description, environmentFile, user, group, workingDirectory, javaPath, runnableJarPath)
    FileSupport.writeToFile(systemdServiceFile, systemdServiceContents)
  }

  def contents(description: String, environmentFile: String, user: String, group: String, workingDirectory: String, java: String, javaExecutable: String): String =
    s"""
       |[Unit]
       |Description=$description
       |After=network.target
       |
       |[Service]
       |EnvironmentFile=$environmentFile
       |User=$user
       |Group=$group
       |PermissionsStartOnly=true
       |WorkingDirectory=$workingDirectory
       |ExecStart=$java $$JAVA_OPTS -jar $javaExecutable
       |
       |[Install]
       |WantedBy=multi-user.target
       |""".stripMargin

}
