package com.xiningli.scalapractice.lectures.errorhandling
import scala.concurrent.Future
import scala.util.{Failure, Random, Success, Try}

object ErrorHandling extends App{

  def potentialError(x: Int) = {
    if (x==0) throw new IllegalArgumentException("really deep rabbit hole!")
    x
  }

  val potentialFunc = (x:Int)=>Try(potentialError(x)) match {
    case Success(s) => println("success with: "+s)
    case Failure(e) => println(e.getMessage)
  }
  potentialFunc(2)
  potentialFunc(0)

}
