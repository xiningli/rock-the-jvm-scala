package com.xiningli.scalapractice.lectures.concurrency

import scala.collection.mutable
import scala.util.Random

object InterThreadJvmCommunication extends App {
  /*
    producer-consumer problem
    producer -> [ x ], setting a value inside of the container
    consumer  extract from the container

    forcing the thread to run in certain order
   */

  class SimpleContainer {
    private var value: Int = 0
    def isEmpty: Boolean = value==0

    def set(newValue: Int): Unit = value = newValue

    def get() = {
      val result = value
      value = 0
      result
    }// this is consumer method
  }


  /** Note
    * please be very clear that the wait and notify methods only work in the synchronized block
    * otherwise it will crash your program
    *
    * general assumptions about synchronized
    *   make no assumptions about who gets the lock first
    *   keep locking to a minimum
    *   maintain thread safety all times in parallel applications
    *
    */

  def naiveProdCons():Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(()=>{
      println("consumer waiting")

      this.synchronized(()=>{
        while(container.isEmpty) {

          /*
          waiting on an object's monitor, release the lock,  suspend you (the thread) indefinitely
          and when it got waits, it will

          RELEASE the Lock
          Suspend

          After it is allowed to proceed, it will take the lock of the monitor again

           */

          this.wait()
          println("[consumer] still actively waiting")
        }
        println("[consumer] I have consumed " + container.get)
      }).apply()

    })
    val producer = new Thread(()=>{
      this.synchronized({
        println("[producer] computing, doing hard work")
        Thread.sleep(500)
        val value = 42
        println("[producer] I have produced, after long work, the value: " + value)
        container.set(value)
        this.notify()
      })
    })

    consumer.start()
    producer.start()
  }
//  naiveProdCons()

  def smartProdCons(): Unit = {
    val container = new SimpleContainer()
    val consumer = new Thread(()=>{
      println("[consumer] waiting")
      container.synchronized{
        container.wait()//will release the lock on the container
      }
      // at this point container must have some value
      // because the only way consumer can be waked up
      // will result the container to have some value
      println("[consumer] I have consumed " + container.get())
      }
    )
    val producer = new Thread(()=>{
      println("[producer] Hard at work")
      Thread.sleep(2000)
      val value = 42
      container.synchronized{
        println("[producer] I am producing value: " + value)
        container.set(value)
        container.notify()
      }
    })
    consumer.start()
    producer.start()
  }
//  smartProdCons()

  // more complicated producer and consumer problem
  /*

   */
  /*
  instead of simple container
  we have a buffer,
  producer produces values in buffer, and consumer consumes values in buffer
   */

  def producerConsumer(): Unit = {
    val buffer = scala.collection.mutable.Queue[Int]()
    buffer.enqueue(-1)
    val capacity = 3
    val consumer = new Thread(()=>{
      val random = new Random()
      while(true) {
        buffer.synchronized{
          println("from consumer: " + buffer)
          if(buffer.isEmpty) {
            println("consumer buffer empty, waiting")
            buffer.wait()
          }

          // at this time, I am free to consumer
          // because it must be at least 1 value.
          // because buffer is synchronized
          // and checked it is not empty

          val x = buffer.dequeue()

          println("Consumer consumed the value " + x)
          // but when notify, it is just waking the thread up
          // and itself has to release the lock, and the other thread can use the lock
          // ways to release the lock including wait or exit the synchronized block
          buffer.notify()
          println("after notify, sleep for a while")
          Thread.sleep(10000)
        }
        Thread.sleep(Math.abs(random.nextInt(200)))
      }
    })

    val producer = new Thread(()=>{
      val random = new Random
      var i = 0
      while(true) {
        buffer.synchronized{
          println("from producer: " + buffer)
          if (buffer.size == capacity) {
            println("i am producer, buffer is full, waiting")
            buffer.wait()
          }
          // at this point, there is at least one space available
          println("i am producer, producing number i: "+ i)
          buffer.enqueue(i)

          buffer.notify()
          i+=1
        }
        Thread.sleep(Math.abs(random.nextInt(1000)))
      }
    })
    consumer.start()
    producer.start()
  }

//  producerConsumer()
  /*
    prod-cons, level 3
    multiple producer and multiple consumer acting on the same buffer

   */
  class consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit ={
      val random = new Random()
      while(true) {
        buffer.synchronized{
//          println(s"[consume $id]: " + buffer)

          /*
          why change to the while loop is because it can be another consumer waking it up
          and after the wait finishes, it goes to buffer and dequeue, which is wrong.
           */
          while(buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting")
            buffer.wait()
          }

          // at this time, I am free to consumer
          // because it must be at least 1 value.
          // because buffer is synchronized
          // and checked it is not empty

          val x = buffer.dequeue()

          println(s"[consumer $id]" + x)
          // but when notify, it is just waking the thread up
          // and itself has to release the lock, and the other thread can use the lock
          // ways to release the lock including wait or exit the synchronized block
          buffer.notify()
        }
        Thread.sleep(Math.abs(random.nextInt(500)))
      }
    }
  }
  class producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {

    override def run(): Unit = {
      val random = new Random
      var i = 0
      while(true) {
        buffer.synchronized{
//          println(s"[producer $id]: " + buffer)
          while (buffer.size == capacity) {
            println(s"[producer $id], buffer is full, waiting")
            buffer.wait()
          }
          // at this point, there is at least one space available
          println(s"[producer $id]"+ i)
          buffer.enqueue(i)

          buffer.notify()
          i+=1
        }
        Thread.sleep(Math.abs(random.nextInt(250)))
      }
    }
  }


  def producerConsumerMultiple(nCons: Int, nProds: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]()
    val capacity = 3
    (1 to nCons).foreach(i=> new consumer(i, buffer).start())
    (1 to nProds).foreach(i=> new producer(i, buffer, capacity).start())

  }

  producerConsumerMultiple(6,3)
}



