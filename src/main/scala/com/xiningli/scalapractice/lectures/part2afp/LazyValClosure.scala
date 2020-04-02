package com.xiningli.scalapractice.lectures.part2afp

object LazyValClosure extends App{
  def add (x: Int, y: Int): Int = {
    x+y
  }
  // you can put a closure here

  lazy val closure = {
    println("defining x, wait 5 sec")
    Thread.sleep(5000)
    2
  }

  println("about to print")
  Thread.sleep(5000)

  println(add(closure, {
    val y = 2
    println("printing y")
    y
  }))

}
