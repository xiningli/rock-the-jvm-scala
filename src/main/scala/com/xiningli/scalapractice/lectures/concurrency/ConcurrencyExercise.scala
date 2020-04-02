package com.xiningli.scalapractice.lectures.concurrency

object ConcurrencyExercise extends App {
  // an example that notifyAll acts in a different way than notify

  def testNotifyAll(): Unit = {
    val bell = new Object()
    (1 to 10).foreach(i => new Thread(()=>{
      bell.synchronized{
        println(s"[thread $i] waiting ...")
        Thread.sleep(1000)
        bell.wait()
        Thread.sleep(1000)
        println(s"[thread $i] awake")
      }
    }).start())
    new Thread(()=>{
      Thread.sleep(20000)
      println("[announcer] Ring the bell")
      bell.synchronized{bell.notifyAll()}
    }).start()
  }
//  testNotifyAll()

  // create a deadlock
  // bowing.... Japanese
  // Japanese will bow to each other and only rise up when the other person rise up

  case class Friend(name: String) {
    def bow(other: Friend):Unit = {
      if (other==this) {
        throw new RuntimeException("can't bow to self")
      }
      this.synchronized{
        println(s"$name: I am bowing to my friend $other")
        other.rise(this)
        println(s"$name: my friend other has risen")
        println(s"I $name am rising")
      }
    }
    def rise(other: Friend):Unit = {
      if (other==this) {
        throw new RuntimeException("can't rise to self")
      }
      this.synchronized{
        println(s"$name: I am rising to my friend $other")
      }
    }

    var side = "right"
    def swichSide():Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend):Unit = {
      if (this==other) throw new RuntimeException("can't pass self")
      while(this.side == other.side) {
        println(s"$name: Oh, but please, $other, feel free to pass...")
        swichSide()
        Thread.sleep(1000)
      }
      println(s"$name: Finally got into the way that I want to go")
    }
  }

  val tomoyoki: Friend = Friend("Tomoyoki")
  val yamato: Friend = Friend("Yamato")
  // inside of tomoyoki's bow's lock, it is waiting for yamato's lock, but tomoyoki didn't release his own lock
  // and inside of the yamato's bow's lock, it is calling tomoyoki's rise. but tomoyoki's rise has been synchronized and used's by tomoyoki's bow already
  // so both will not continue
//  new Thread(()=>tomoyoki.bow(yamato)).start()
//  new Thread(()=>yamato.bow(tomoyoki)).start()
//

  // 3- livelocks
  new Thread(()=>tomoyoki.pass(yamato)).start()
  new Thread(()=>yamato.pass(tomoyoki)).start()


}

