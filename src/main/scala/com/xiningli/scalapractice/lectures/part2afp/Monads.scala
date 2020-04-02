package com.xiningli.scalapractice.lectures.part2afp


/*

list of elements..
Monads are a kind of types which have some fundamental operations.
Like a list
 */




object Monads extends App {
  trait MonadTemplate[A] {
    def unit(value:A): MonadTemplate[A] // also called pure or apply
    def flatMap[B](f: A=> MonadTemplate[B]): MonadTemplate[B]
  }

  // list, option, try, future,

  // Operations must satisfy the Monad laws

  // left-identity
  // unit(x).flatMap(f) = f(x)
  // List(x).flatMap(f) = f(x) ++ Nil.flatMap(f) = f(x)

  // right-identity
  // aMonadInstance.flatMap(unit) == aMonadInstance
  // associativity
  // m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))



}
