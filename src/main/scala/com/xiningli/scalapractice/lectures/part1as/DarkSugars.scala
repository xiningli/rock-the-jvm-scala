package com.xiningli.scalapractice.lectures.part1as

import scala.util.Try
import scala.reflect.runtime.universe._

object DarkSugars extends App { // presentation mode

  def printType[T](x: T)(implicit tag: TypeTag[T]): Unit = println(tag.tpe.toString)

  // Syntax sugar #1: methods with single param

  def singleArgMethod(arg: Int): String = s"$arg little docks"

  val description = singleArgMethod {
    // write some complex code here
    42
  }

  val aTryInstance = Try { // java's try
    throw new RuntimeException
  }

  println(aTryInstance)

  val list0 = List(1, 2, 3).map { x =>
    val num = 1
    x + num
  }
  println(list0)

  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    def act(x: Int) = x + 1
  }

  val aFunkyInstance0 = (x: Int) => x + 1
  printType(aFunkyInstance0) //result: Int => Int


  val aFunkyInstance1: Action = (x: Int) => x + 1 // MAGIC !!!
  printType(aFunkyInstance1) // result: com.xiningli.scalapractice.lectures.part1as.DarkSugars.Action

  // example: Runnables
  // has only one method yet to be implemented
  val aThread = new Thread(new Runnable {
    def run() = println("hello Scala")
  })

  val aSweeterThread = new Thread(() => println("sweet, scala"))
  aSweeterThread.start()

  abstract class AnAbstractType {
    def implemented: Int = 23

    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")


  // syntax sugar #3 the :: and #:: are special


  val prependedList = 2 :: List(3, 4, 5)


  println(prependedList)


  println(List(1, 2).::(0))
  // in scala if a method ends with :, then it is right associative......
  // so we have the 1::2::3::List(4,5) to be evaluated as

  println(1 :: (2 :: (3 :: List(4, 5))))

  class MyStream[T]{
    def -->:(value:T):MyStream[T] = this
  }

  val myStream = 1-->: 2-->: new MyStream[Int]

  // Syntax sugar #4: multi-word method naming


  class TeenGirl(name:String){
    def `and then said`(gossip:String) = println(s"$name said $gossip")
  }


  val lilly = new TeenGirl("lily")
  lilly `and then said` "scala is so sweet!"

  class Composite[A,B]

//  val composite: Int Composite String = ???

  // syntax sugar #6, update() is very special, much like apply()

  val anArray = Array(1,2,3)
  anArray(2)=7// rewritten to anArray.update(2,7)

  // used in mutable collections


  // syntax sugar #7: setters for mutable containers
  class Mutable{
    private var internalMembers: Int = 0// private for oo encapsulation
    def member = internalMembers
    def member_=(value: Int):Unit = internalMembers=value//setter
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten as aMutableContainer.member_=(42)
  println(aMutableContainer.member)
}
