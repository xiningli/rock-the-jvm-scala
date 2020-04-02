package com.xiningli.scalapractice.exercises

import scala.annotation.tailrec

trait MySet[A] extends (A=>Boolean){
  def apply(elem:A):Boolean =
    contains(elem)// when used to call the set(123) set("whatever")
  def contains(elem: A):Boolean
  def + (elem:A):MySet[A]//Adding a new element
  def ++ (anotherSet:MySet[A]):MySet[A]// union
  def map[B](f:A=>B):MySet[B]
  def flatMap[B](f:A=>MySet[B]):MySet[B]
  def filter(predicate:A=>Boolean):MySet[A]
  def forEach(f:A=>Unit):Unit


  /*
Exercise,
removing an element
intersecting with another element
difference with another set
 */

  def - (elem:A):MySet[A]
  def -- (anotherSet:MySet[A]):MySet[A]
  def & (anotherSet:MySet[A]):MySet[A]

  // exercise 3 -> implment a unary_! = Negation of a Set

  def unary_! : MySet[A]

}

class AllInclusiveSet[A] extends MySet[A]{
  def contains(elem: A):Boolean = true
  def + (elem:A):MySet[A] = this
  def - (elem:A):MySet[A] = new PropertyBasedSet[A](
    x=>if (x==elem) false else true
  )
  def ++ (anotherSet:MySet[A]):MySet[A]  = this

  /*
  naturals = all_inclusive_set[Int] = all the natural numbers
  naturals.map(x=>x%3)=>???
  [0,1,2]
   */

  def map[B](f:A=>B):MySet[B] = throw new IllegalArgumentException("really deep rabbit hole!")

  def flatMap[B](f:A=>MySet[B]):MySet[B] = throw new IllegalArgumentException("really deep rabbit hole!")

  def filter(predicate:A=>Boolean):MySet[A] = throw new IllegalArgumentException("really deep rabbit hole!")

  def forEach(f:A=>Unit):Unit = throw new IllegalArgumentException("really deep rabbit hole!")



  /*
Exercise,
removing an element
intersecting with another element
difference with another set
 */

  def -- (anotherSet:MySet[A]):MySet[A] = filter(!anotherSet)
  def & (anotherSet:MySet[A]):MySet[A] = filter(anotherSet)

  // exercise 3 -> implment a unary_! = Negation of a Set

  def unary_! : MySet[A] = new EmptySet[A]

}

// all elements of type A which satisfy a property
// {x in A | property(x)}
class PropertyBasedSet[A](property: A=>Boolean) extends MySet[A]{
  def contains(elem: A):Boolean = property(elem)
  // {x in A|property(x)} + element  = {x in A|property(x)|| x==element}
  def + (elem:A):MySet[A] =
    new PropertyBasedSet[A](x=>property(x)||x==elem)
  def ++ (anotherSet:MySet[A]):MySet[A] =
    new PropertyBasedSet[A](x=>property(x)||anotherSet(x))
  // all integers =>(x%3) =>[0,1,2]
  def map[B](f:A=>B):MySet[B] = politelyFailed
  def flatMap[B](f:A=>MySet[B]):MySet[B]= politelyFailed
  def filter(predicate:A=>Boolean):MySet[A] = new PropertyBasedSet[A](x=>property(x)&&predicate(x))
  def forEach(f:A=>Unit):Unit = politelyFailed
  def - (elem:A):MySet[A] = filter(x=> x!=elem)
  def -- (anotherSet:MySet[A]):MySet[A] = filter(!anotherSet)
  def & (anotherSet:MySet[A]):MySet[A] = filter(anotherSet)
  def unary_! : MySet[A] = new PropertyBasedSet[A](x=> !property(x))

  def politelyFailed = throw new IllegalArgumentException("really deep rabbit hole!")

}


// singly linked set
class EmptySet[A] extends MySet[A]{
  def contains(elem: A)=false
  def + (elem:A):MySet[A] = new NonEmptySet(elem, this)
  def ++ (anotherSet:MySet[A]) = anotherSet
  def map[B](f:A=>B):MySet[B] = new EmptySet[B]
  def flatMap[B](f:A=>MySet[B]):MySet[B] = new EmptySet[B]
  def filter(predicate:A=>Boolean):MySet[A] = this
  def forEach(f:A=>Unit):Unit = ()=>{}
  def - (elem:A):MySet[A] = this
  def -- (anotherSet:MySet[A]):MySet[A] = this
  def & (anotherSet:MySet[A]):MySet[A] = this


  def unary_! :MySet[A] = new PropertyBasedSet[A](_=>true)


}

class NonEmptySet[A](head:A,tail:MySet[A]) extends MySet[A] {
  def contains(elem:A): Boolean =
    elem == head || tail.contains(elem)
  def + (elem:A): MySet[A] =
    if (this contains elem) this
    else new NonEmptySet(elem, this)
  def ++ (anotherSet:MySet[A]) =
    tail ++ anotherSet + head

  def - (elem:A):MySet[A] =
    if (head==elem) tail
    else tail - elem + head

  def -- (anotherSet:MySet[A]):MySet[A] = filter(x=> !anotherSet.contains(x))

  def & (anotherSet:MySet[A]):MySet[A] =
    filter(x=>anotherSet.contains(x))//intersecting and filtering are the same thing.


  /**
    * [1,2,3]++[4,5]
    * [2,3]++[4,5]+1
    * [3]++[4,5]+1+2
    * [3]++[4,5]+1+2
    * [] ++ [4,5] + 1 + 2 + 3
    * [] is already the EmptySet, so that we have
    * [4,5] + 1 + 2 + 3
    * and
    * [1,2,3,4,5]
    */
  def map[B](f: A => B):MySet[B] =
    tail.map(f) + f(head)
  def flatMap[B](f: A =>MySet[B]):MySet[B] =
    f(head) ++ tail.flatMap(f)
  def filter(predicate:A=>Boolean):MySet[A]={
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def forEach(f:A=>Unit):Unit={
    f(head)
    tail forEach f
  }

  def unary_! : MySet[A] = new PropertyBasedSet[A](x=> ! this.contains(x))



}

object MySet{

  /*
  val s = MySet(1,2,3) = buildSet(seq(1,2,3),[]) =
  buildSet(seq(2,3),[]+1)
   = buildSet(seq(3),[1]+2)
      = buildSet(seq(),[1,2]+3)
  = [1,2,3]
    */
  def apply[A](values:A*):MySet[A] ={
    @tailrec
    def buildSet(valSeq:Seq[A],acc:MySet[A]):MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    buildSet(values.toSeq, new EmptySet[A])
  }
}


object MySetPlayground extends App{
  val s = MySet(1,2,3,4)
  s + 5 ++MySet(-1,-2,3) flatMap (x=>MySet(x,10*x)) filter(_%2==0) forEach println

  println("--------------- testing -")

  (s - 2) forEach println

  println("--------------- testing --")
  val s2 = MySet(3,5)

  (s -- s2) forEach println


  println("--------------- testing &")
  (s & s2) forEach println


  val negative = !s + 123
  println(negative)
  println(negative(2))
  println(negative(123))

}