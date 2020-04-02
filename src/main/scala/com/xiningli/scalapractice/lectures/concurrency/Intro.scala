package com.xiningli.scalapractice.lectures.concurrency

import java.util.concurrent.Executors

object Intro extends App{

  /**
    * interface runnable has a method run
    * public void run()
    */

  // JVM threads
  // thread is an instance of a class
  val aThread = new Thread(()=>println("Running in parallel"))
  aThread.start()// gives the signal to the JVM to start a Jvm thread
  aThread.join()// blocks until a thread finished running
  val threadHello = new Thread(()=>(1 until 6).foreach(e=>println("hello")))
  val threadBye = new Thread(()=>(1 until 6).foreach(e=>println("bye")))

  threadHello.start()
  threadBye.start()

  // different runs produce different results
  // threads are expensive to create and destroy.
  // so java library offers the executor to reuse the threads.
  val pool = Executors.newFixedThreadPool(10);
  pool.execute(()=>println("something in the thread pool"));

  pool.execute(()=>{
    Thread.sleep(1000)
    println("done after 1 sec ")
  })


  pool.execute(()=>{
    Thread.sleep(1000)
    println("almost done ")
    Thread.sleep(2000)
    println("done after 2 sec ")

  })

//  pool.shutdown()
//  pool.execute(()=>println("should not appear"))// because the pool is shutdown
//  pool.shutdownNow() // will have sleep interrupted in the slept thread

  // but the shutdown will just stop taking more thread

  pool.shutdown()

  def runInParallel: Unit = {
    var x=0
    var thread1 = new Thread(()=>x=1)
    var thread2 = new Thread(()=>x=2)
    thread1.start()
    thread2.start()
    println(x)
  }
//  (1 to 10000).foreach(e=>runInParallel)
  // this is the race condition

  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
    def getAmount: Int = this.synchronized(amount)
  }

  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    account.synchronized({
      account.amount -= price
    })

//    println("I've bought " + thing)
//    println("my account is now " + account)
  }

  for (_<- 1 to 1000000) {
    val account = new BankAccount(50000)
    val thread1 = new Thread(()=>buy(account, "shoes", 3000))
    val thread2 = new Thread(()=>buy(account, "iphone12", 4000))
    thread1.start()
    thread2.start()
    thread2.join()// have to join otherwise the account.getAmount can be executed first.
    thread1.join()
    val tmpAmount = account.getAmount
    if (tmpAmount!=43000)
      println("account:" + tmpAmount)
  }
  // can potentially printout account:47000 or account:46000
  /** with the following definition
    * def buy(account: BankAccount, thing: String, price: Int): Unit = {
    *     account.amount -= price
    *
    * //    println("I've bought " + thing)
    * //    println("my account is now " + account)
    * }
    */

  /** Exercise
    * 1) construct 50 inception threads
    * thread1->thread2->thread3...
    * println("hello from thread #3")
    * in reverse order
    *
    */

  var x=0
  val threads = (1 to 100).map(_=>new Thread(()=>x+=1))
  threads.foreach(_.start)
  /*
    * what is the biggest value  possible for x?
    * what is the smallest value possible for x?
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
  // what's the value of the message?

}
