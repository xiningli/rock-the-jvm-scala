package com.xiningli.scalapractice.lectures.part1as
import scala.annotation.tailrec
import scala.reflect.runtime.universe._

object Recap extends App {
  def printType[T](x: T)(implicit tag: TypeTag[T]): Unit = println(tag.tpe.toString)

  val condition: Boolean = false
  val value = if(condition) 42 else 65
  // instructions vs expressions
  // instructions are executed in sequence
  // but in scala, we are going to use the expressions

  // scala compiler infers the type for us
  val codeBlock = {
    if (condition) 42
    65
  }//   printType(codeBlock) suggests that it is a string

  val theUnt = println("hello scala")
  //this will return the uit
  printType(theUnt)

  @tailrec def factorial(n:Int, accumulator: Int): Int ={
    if (n<=0) accumulator
    else factorial(n-1, n* accumulator)
  }

  println(factorial(6,1))

  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog //subtyping.
  trait Carnivore {
    def eat(a:Animal): Unit// they will be implemented later


  }

  class Crocodile extends Animal with Carnivore{
    def eat(a:Animal) = println("crunch " + a);
  }

  // method notations
  val aCroc= new Crocodile
  aCroc eat aDog


  // anonomous classes

  val aCarnivore= new Carnivore{
    def eat(a:Animal)= println("roar")
  }
  printType(aCarnivore)

  //generic
  abstract class MyList[+A]//variance and covariance

  // singleton object and companion object

  //denoted by the keyword object
  object MyList

  // case classes light weight data classes already have companion objects
  case class Person(name:String, age:Int)

  def throwException = throw new RuntimeException// type is Nothing, and cannot be extended
  val aPotentialFailure = try{
    throw new RuntimeException
  }catch{
    case e:Exception => "I caught an exception"
  }finally {
    println("some logs")
  }

  // packaging and imports

  // functional programming

  println("some logs")

  val incrementer = new Function1[Int,Int]{
    def apply(v1: Int) = v1 + 1
  }
  println (incrementer(1))

  // reduced as the syntatic sugar!!!
  val anonymoutIncrementer = (x:Int)=>x+1
  println (anonymoutIncrementer(1))

  List(1,2,3).map(anonymoutIncrementer) // map is a HOF

  // map, flatMap, filter

  // for comrehension

  val pairs = for{
    num <- Vector(1,2,3)
    char <- Vector('a','b','c')
    if num<3
  } yield num + "-" + char

  println(pairs)


  val aMap = Map(
    "Xining"->23,
    "Jiaoyue"->24
  )
  println(aMap)

  // "collections": Opitons, try

  val anOption = Some(2)

  // pattern matching

  val x=2
  val order = x match{
    case 1=> "first"
    case 2 => "second"
    case 3=> "third"
    case _ => x + "th"
  }

  val bob = Person("Bob",22)
  val greeting =bob match{
    case Person(n, _) => s"Hi, My name is $n"// pattern matching with stuff
    // reused started with s Interpolated String
  }

  println(greeting)

}