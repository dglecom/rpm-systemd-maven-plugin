package de.douglas.maven.plugin.rpmsystemd

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

object FileSupport {

  def writeToFile(file: Path, contents: String): Path = Files.write(file, contents.getBytes(StandardCharsets.UTF_8))

}
