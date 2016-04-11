package de.douglas.maven.plugin.rpmsystemd

import java.nio.file.Path

object SystemPaths {

  private val targetDir = "target"
  private val pluginDirName = "rpm-systemd-maven-plugin"

  val postInstFileName = "postinst"

  def generationRootDir(baseDir: Path): Path = baseDir.resolve(targetDir).resolve(pluginDirName)

}
