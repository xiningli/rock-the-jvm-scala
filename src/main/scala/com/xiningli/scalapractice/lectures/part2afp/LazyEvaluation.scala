package com.xiningli.scalapractice.lectures.part2afp

object LazyEvaluation extends App {

  val eager: String = {
    println("Hi i am eager")
    "eager"
  }

  lazy val str: String = {
    println("Hi i am so lazy just got here")
    "lazy"
  }

  println(str)

  lazy val x0: Int = throw new RuntimeException
  // lazy delays the evaluation of values
  // so even we let it to throw an exception, it is still ok
  lazy val x: Int = {
    println("hello")
    42
  }
  // lazy value evaluated once but only when they are used.
  println(x)
  println(x) //here x will not be evaluated again. due to the lazy evaluation, it will just use the x's value

  // examples of implications:

  def sideEffectCondition: Boolean = {
    println("boo")
    true
  }

  def simpleCondition: Boolean = false


  lazy val lazyCondition = sideEffectCondition

  println(if (simpleCondition && lazyCondition) "yes" else "no") //shortcutting
  // because the false is already there.

  // becareful if your program has side effect....
  println(if (lazyCondition && simpleCondition) "yes" else "no")


  def byNameMethod(n: => Int): Int = {
    n + n + n + 1
  }

  println("start of experiment")

  def retrieveMaticValue = {
    //side effect for a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMaticValue))

  println("in between")
  Thread.sleep(1000)

  def byNameMethod2(n: => Int): Int = {
    // call by need

    val t = n //    lazy val t = n // lazy vals are only evaluated once.

    t + t + t + 1
  }



  // here we are waiting only once, because lazy vals only evaluated once.
  println(byNameMethod2(retrieveMaticValue))

  println("in between 2")

  def byValueMethod(t: Int): Int = {
    // call by need

    t + t + t + 1
  }

  println(byValueMethod(retrieveMaticValue))


  println("end of experiment ")

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }


  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // list
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // uses the lazy vals under the hood
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20)
  println("------------")

  println(gt20Lazy)

  println("------------")

  gt20Lazy.foreach(println)

  /*
  exercise: implement a lazilly evaluated linked stream of elements

   */


  // for comprehension uses the filter monaniac


  val compFor = for{
    a<-List(1,2,3) if a%2==0
  } yield a+1

  // is equivalent to the following equation

  val withFilMap = List(1,2,3).withFilter(a => a%2==0).map(a => a+1)

  println(compFor)
  println(withFilMap)

  abstract class MyStream[+A] { //covariance
    def isEmpty: Boolean

    def head: A

    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend operator
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] //concatenate two streams
    def foreach(f: A => Unit): Unit

    def map[B](f: A => B): MyStream[B]

    def flatMap[B](f: A => MyStream[B]): MyStream[B]

    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A]

    def takeAsList(n: Int): List[A]
  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }

//  MyStream.from(1)(x=>x+1) = stream of natural numbers (potentially infinite)
//natural.take(100) // lazilly evaluated stream of the first 100 natural numbers (finite stream)
// natural.foreach(println) // this will crash because this is infinite
// natural.map(_ *2)// stream of all even numbers (potentially infinite)



}
