package com.xiningli.scalapractice.lectures.part2afp

object PartialFunctions extends App{
  val aFunction = (x:Int)=>x+1// function1[Int,Int] == Int=>Int as the sugar type
  class FunctionNowApplicableException extends RuntimeException

  val aFussyFunction =(x:Int)=>{
    if(x==1) 42
    else if (x==2) 56
    else throw new FunctionNowApplicableException
  }


  val aNicerFussyFunction = (x:Int)=> x match {
    case 1=>42
    case 2 => 56
    case 5 => 99
  }



  val aTotalFunction = (x:Int)=> x match {
    case 1=>42
    case 2 => 56
    case 5 => 99
  }
  println(aNicerFussyFunction(5))

  val aPartialFunction:PartialFunction[Int,Int] ={
    case 1=>42
    case 2 => 56
    case 5 => 99
  }// including the curly brace is called a partial function value
  println(aPartialFunction(2))// involking the apply method

// PartialFunction is based on pattern matching
// partial functions utilizes
  println(aPartialFunction.isDefinedAt(67))
  // lifted
  val lifted = aPartialFunction.lift // Int->Option[Int]
  // val lifted2 = aTotalFunction.lift cannot do it ...
  println(lifted(2))
  println("98" + lifted(98))

  val pfChain = aPartialFunction.orElse[Int,Int]{
    case 45=>67// argument from the second function
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal function

  val aTotalFunction2:Int=>Int = {
    case 1=>99
  }

  val aMappedList = List(1,2,3).map{
    case 1=>42
    case 2=>78
    case 3=>1000
  }

  println(aMappedList)

  /*
  Note: PF can only have ONE parameter type
   */

  /**
    * com.xiningli.scalapractice.exercises
    * 1. construct a PF instance yourself, (anonymous class)
    * 2. implement a dump chatbot as a PF
    */

  val aManualFussyFunction: PartialFunction[Int,Int] = new PartialFunction[Int,Int]{
    def apply(x:Int):Int = x match{
      case 1=>42
      case 2=>65
      case 5=>999
    }
    def isDefinedAt(x:Int):Boolean =
      x==1||x==2||x==5
  }

  val chatBot:PartialFunction[String,String] = {
    case "hello"=>"My name is HAL90000"
    case "goodbye"=>"asdljqwjep"
    case "call mom"=>"unable to find your phone"
  }
//  scala.io.Source.stdin.getLines().foreach(line => println("chatBotSaid:" + chatBot(line)))
  scala.io.Source.stdin.getLines().map(chatBot).foreach(println)





}
