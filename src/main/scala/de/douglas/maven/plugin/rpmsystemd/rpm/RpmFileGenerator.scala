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

package de.douglas.maven.plugin.rpmsystemd.rpm

import java.io.File
import java.nio.file.Path

import de.douglas.maven.plugin.rpmsystemd.{FileSupport, SystemPaths}

class RpmFileGenerator {

  def generatePostInstFile(baseDir: File, environmentFile: String, workingDirectory: String, serviceFileName: String,
                           user: String, group: String, additionalDirectories: Iterable[DirectoryConfiguration]): Path = {

    val generatedResourcesDir = SystemPaths.generationRootDir(baseDir.toPath)
    generatedResourcesDir.toFile.mkdir()

    val postInstFile = generatedResourcesDir.resolve(SystemPaths.postInstFileName)

    val postInstContents = contents(user, group, serviceFileName, environmentFile, workingDirectory, additionalDirectories)
    FileSupport.writeToFile(postInstFile, postInstContents)
  }

  def contents(user: String, group: String, serviceFileName: String, environmentFile: String, workingDirectory: String,
               additionalDirectoriesConfiguration: Iterable[DirectoryConfiguration]): String = {

    val mandatoryScript = s"""
       |${echo(environmentFile, """JAVA_OPTS=""""")}
       |${addgroup(group)}
       |${adduser(user, group, "/bin/false", workingDirectory)}
       |
       |${mkdir(workingDirectory)}
       |${chown(workingDirectory, user, group)}
       |
       |${presetSystemd(serviceFileName)}
       |""".stripMargin

    val additionalDirectoriesScript = additionalDirectoriesConfiguration.map { dirConf =>
      s"""
         |${mkdir(dirConf.getDirectory)}
         |${chown(dirConf.getDirectory, dirConf.getUser, dirConf.getGroup)}
         |""".stripMargin
    }.mkString("")
    
    mandatoryScript + additionalDirectoriesScript
  }

  def mkdir(dir: String): String = s"test -d $dir || mkdir $dir >/dev/null 2>&1"

  def chown(dir: String, user: String, group: String): String = s"test -d $dir && chown -R $user:$group $dir >/dev/null 2>&1"

  def addgroup(group: String): String = s"getent group $group || groupadd -r $group >/dev/null 2>&1"

  def adduser(user: String, group: String, shell: String, homeDir: String): String = s"getent passwd $user || useradd -s $shell -d $homeDir -g $group -r -m $user >/dev/null 2>&1"

  def presetSystemd(serviceFile: String): String = s"/usr/bin/systemctl preset $serviceFile >/dev/null 2>&1"

  def echo(file: String, text: String): String = s"""test -f $file || echo '$text' > $file"""
}
