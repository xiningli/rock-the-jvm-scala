package com.xiningli.scalapractice.lectures.part2afp

object ForComprehensions extends App {
  for {
    i <- List("a", "b", "c")
    j <- List(1,2,3)
  } println(i + j)
}
