package com.xiningli.scalapractice.lectures.part2afp
import scala.reflect.runtime.universe._

object PartiallyAppliedFunctionVsCurries  extends App{
  def printType[T](x: T)(implicit tag: TypeTag[T]): Unit = println(tag.tpe.toString)

  println("--------partially applied function vs curries")

  def add(i: Int, j: Int) = i + j
  val add5 = add(_: Int,5)
  println(add5(2))

  /** Creates a tupled version of this function: instead of 2 arguments,
    *  it accepts a single [[scala.Tuple2]] argument.
    *
    *  @return   a function `f` such that `f((x1, x2)) == f(Tuple2(x1, x2)) == apply(x1, x2)`
    */
  val addTupled = ((x:Int,y:Int)=>add(x,y)).tupled
//  val addTupled = (add _).tupled

  printType(addTupled)
  println(addTupled(2,3))
  println(List((1,2), (4,5), (3,8)).map((add _).tupled))

  val addCurried = (add _).curried
  val res = List(1,4,3).map(addCurried)
  printType(res.head)
  println(res)
  println(res.head(2))
}
