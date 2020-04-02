package com.xiningli.scalapractice
import scala.reflect.runtime.universe
import scala.tools.reflect.ToolBox

object Hi {
  def main(args: Array[String]): Unit = {
    val tb = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
    val command = scala.io.Source.fromInputStream(System.in).mkString
    val parsed = tb.parse(command)
    val res = tb.eval(parsed)
    println(res)
  }
}
