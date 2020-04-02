package com.xiningli.scalapractice.lectures.concurrency

import java.util.concurrent.atomic.AtomicInteger

object IntroExercise extends App{

  /** Exercise
    * 1) construct 50 inception threads
    * thread1->thread2->thread3...
    * println("hello from thread #3")
    * in reverse order
    *
    */
  def inceptionThread(maxThread:Int, i:Int=0):Thread = new Thread(()=>{
    if (i<maxThread) {
      val newThread = inceptionThread(maxThread, i+1)
      newThread.start() // this is basically a stack recursive call
      newThread.join() // because everything is joined all together
    }
    println(s"Hello from thread $i")
  })

  inceptionThread(50).start()

  var x=new AtomicInteger(0)
  val threads = (1 to 100).map(_=>new Thread(()=>x.incrementAndGet()))
  threads.foreach(t=>{
    t.start()
    t.join()
  })
  println(x)
  /*
    * what is the biggest value  possible for x? 100
    * what is the smallest value possible for x? 1
    *
    * thread 1: x=0
    * thread 2: x=0
    * thread 3: x=0
    * all intends to write x=1 at the same time
    */
  /*
  3 sleep fallacy
   */
  var message = ""
  val awesomeThread = new Thread(()=>{
    Thread.sleep(1000)
    message = "scala is awesome"
  })

  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)
  // what's the value of the message? almost always "scala is awesome, but it is not guaranteed"
  // never do this, because the cpu may schedule task in between. sleep is only the minimum time to wait
  // so sleep
  /** What's the value of the message?
    * (main thread)
    *   message = "scala sucks"
    *   sleep() relieves the execution
    *  (awesome thread started)
    *   sleep()  relieves the execution
    *  (OS gives the CPU time to do important things, take CPU more than 2 sec)
    *  OS CPU back to the MAIN thread
    *  print(scala sucks)
    *
    *  (OS gives the CPU to the awesome thread)
    *
    */

}
