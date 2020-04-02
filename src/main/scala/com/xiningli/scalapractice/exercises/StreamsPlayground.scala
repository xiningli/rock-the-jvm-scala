package com.xiningli.scalapractice.exercises

import com.xiningli.scalapractice.exercises.StreamsPlayground.prev

import scala.annotation.tailrec

abstract class MyStream[+A] { //covariance
  def isEmpty: Boolean

  def head: A

  def tail: MyStream[A]


  // the following B >: A means B is a super type of A
  def #::[B >: A](element: B): MyStream[B] // prepend operator
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] //concatenate two streams
  def foreach(f: A => Unit): Unit

  def map[B](f: A => B): MyStream[B]

  def flatMap[B](f: A => MyStream[B]): MyStream[B]

  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A]


  def takeAsList(n: Int): List[A] = take(n).toList()

  /*
  [1,2,3].toList([]) =
  [2,3].tolist([1]) =
  [3].tolist([2,1]) =
  [].tolist([3,2,1])
  = [1,2,3]
   */


  @tailrec
  final def toList [B >: A] (acc: List[B] = Nil):List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)
}

object EmptyStream extends MyStream[Nothing] {
  def isEmpty = true

  def head: Nothing = throw new NoSuchElementException

  def tail: MyStream[Nothing]= throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this) // prepend operator
  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B]= anotherStream //concatenate two streams
  def foreach(f: Nothing => Unit): Unit= () => {}

  def map[B](f: Nothing => B): MyStream[B]= this

  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B]= this

  def filter(predicate: Nothing => Boolean): MyStream[Nothing]= this

  def take(n: Int): MyStream[Nothing]= this

}

class Cons[+A](hd: A, t1: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false

  val head: A = hd
  lazy val tail: MyStream[A] = t1 // call by need


  /*
  val s = new Cons(1, EmptyStream)
  val prepended = 1 #:: s = new Cons(1,s)
   */

  def #::[B >: A](element: B): MyStream[B] = new Cons(element, this) // prepend operator
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B]  = new Cons(head,tail ++ anotherStream)//concatenate two streams
  def foreach(f: A => Unit): Unit = {
    f(hd)
    tail foreach f
  }



  /*
  s = new Cons(1,?)
  mapped s.map(_+1) = new Cons(2, s.tail(_ + 1))
  this will not be evaluated, unless you somehow use the
     ...map.tail in the later expression
   */

  def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail map f) //preserve lazy evaluation

  // this must be also lazily evaluated
  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)

  def filter(predicate: A => Boolean): MyStream[A] = {
    if (predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate) // will preserves the lazy evaluation....
  }

  def take(n: Int): MyStream[A] =
    if (n<=0) EmptyStream
    else if (n==1) return new Cons(head, EmptyStream)
    else new Cons(head, tail.take(n-1))




}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    // lazily evaluated tail.....
    new Cons(start, MyStream.from(generator(start))(generator))
}

object StreamsPlayground extends App{
  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.tail.head)
  val startFrom0 = 0#::naturals
  println(startFrom0.head)

//  println(startFrom0.take(100).foreach(println))
  // map, flatMap
  println(startFrom0.map(_ * 2).take(20).toList())
  println(startFrom0.flatMap(x=>new Cons(x, new Cons(x+1, EmptyStream))).take(10).toList())
  println(startFrom0.filter(_ <10).take(10).take(20).toList())

  // Exercises on streams

  // 1- stream of fibonacci numbers
  var prev = 1

//  val fibs = MyStream.from(1)(x=>{
//
//    def rec(prev: Int, x:Int): Int ={
//      val result = x+prev
//      prev = x
//      result
//    }
//
//  })


  // 2- stream of prime numbers with Eratosthenes' sieve
  // [2,3,4,.....]
  // filter out all numbers divisible by 2
  // [2,3,5,.....]
  // filter out all numbers divisible by 3
  // [2,5,7,11,13,17....]

//  println(fibs.take(10).toList())


}



