package com.xiningli.scalapractice.lectures.part2afp

object CallByNameVsCallByValue {
  def something() = {
    println("calling something")
    1 // return value
  }
  def callByValue(x: Int) = {
    println("x1=" + x)
    println("x2=" + x)
  }

  def callByName(x: => Int) = {
    println("x1=" + x)
    println("x2=" + x)
  }

  callByValue(something())
  callByName(something())

  /**
    * So you can see that in the call-by-value version, the side-effect of the passed-in function call (something()) only happened once. However, in the call-by-name version, the side-effect happened twice.
    *
    * This is because call-by-value
    * functions compute the passed-in
    * expression's value before calling
    * the function, thus the same value
    * is accessed every time.
    *
    * However, call-by-name functions
    * recompute the passed-in expression's
    * value every time it is accessed.
    */

}
