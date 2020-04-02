package com.xiningli.scalapractice.lectures.part1as

object AdvancedPatternMatching  extends App{

  val number = List(1)
  val description = number match {
    case head :: Nil => println(s"the only element is head $head.")
    case _ =>
  }


  /**
    *  - constants
    *  - wildcards like the _
    *  - case classes
    *  - tuples
    *  - special matic like above
    */


  class Person(val name: String, val age: Int)

  // start defining an object

  object Person{
    // unapply is the key word here
    def unapply(person: Person): Option[(String,Int)] =
      if (person.age<21) None
      else Some((person.name, person.age))
    def unapply(age:Int) : Option[String] =
      Some(if(age<21) "minor" else "major")
  } // companion object

  val bob = new Person("Bob", 12)
//  val greeting = bob match {
//    case Person(person) => s"Hi, my name is $person"
//  }
//  println(greeting)


  val legalStatus = bob.age match {
    case Person(status) => s"my legal status is $status"
  }
//  println(greeting)
  println(legalStatus)
  val n: Int = 3
  object singleDigit{
    def unapply(arg: Int) =
      if (arg> -10&&arg<10) Some(arg)
      else None
  }
  val mathProperty = n match {
    case x if x%2==0 => "an even number"
    case singleDigit(s)=>s"single digit + $s"
    case _ =>"no property"
  }
  println(mathProperty)

  // infix patterns
  case class Or[A,B](a:A, b:B)// Either... in scala
  val either = Or(2,"two")
  val humanDescription = either match {
    case number Or string=> s"$number is written as $string"
  }

  println(humanDescription)

  // decomposing sequences

  val numbers = List(1,2,3,4,5,6,7,8,9)
  val vararg = numbers match {
    case List(1,_*) => "starting with 1"
  }
  println(vararg)

  abstract class MyList[+A]{
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]
  case class Cons[+A] (override val head: A,
                       override val tail: MyList[A])
    extends MyList[A]
    // this will turn the MyList[A] into the Seq[A] in which
  // the sequence holds the same elements as in the MyList[A]
  object WhatEverWeWant{
    def unapplySeq[A](list:MyList[A]):Option[Seq[A]] =
      if (list ==Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(tailResult => list.head +: tailResult)
  }
  val myList:MyList[Int] = Cons(1,Cons(2,Cons(3,Empty)))


  // case class type match

  val s =myList match {
    case Cons(1,_)=> "start with 1 by case class"
    case _ => "whatever"
  }

  println(s)

  val decomposed = myList match {
    case WhatEverWeWant(1,2,_*)=>"starting with 1,2"
    case _ => "something else"
  }

  println(decomposed)
  // custom return types for unapply methods
  // isEmpty: Boolean, get: something

  // must have this two methods otherwise the code won't compile
  // throw error messages: wrong number of arguments for extractor
  abstract class Wrapper[T]{
    def isEmpty:Boolean
    def get:T
  }

  object PersonWrapper{
    def unapply(person:Person):Wrapper[String]=
      new Wrapper[String]{
        def isEmpty = false
        def get = person.name
      }
  }

  println(bob match {
    case PersonWrapper(n)=>s"This person's name is $n"
    case _ => "An alien"
  })


  // option test

  val something = Some(1)
  val something2 = something.map(k=>k+1)
  println(something2)
}
