package com.xiningli.scalapractice.lectures.concurrency

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}
import scala.concurrent.ExecutionContext.Implicits.global
object FutureAndPromises extends App{
  // future are functional ways of computing something in parallel
  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future(calculateMeaningOfLife)

  // calculate teh meanings of life on Another tread
  // available here passed by compiler , how implicit work

  println(aFuture.value) // it will return Option[Try[Int]]
  println("waiting on the future")
  aFuture.onComplete(t=> t match {
    case Success(meaningOfLife)=>println(s"the meaning of life is $meaningOfLife")
    case Failure(e)=>println(s"I have failed with $e")
  })// this is the call back function of the future
  // we do not make assumptions about what call the call back function
  Thread.sleep(3000)

  // mini social network
  // everything has to be down asynchronously

  case class Profile(id: String, name: String) {
    def poke (anotherProfile: Profile):Unit = {
      println(s"${this.name} poking ${anotherProfile.name}")
    }
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck"->"fb.id.2-bill"
    )

    val random = new Random()

    // potentially expensive API calls
    def fetchProfile(id: String): Future[Profile] =  {
      // fetching from the db
      Thread.sleep(random.nextInt(300))
      Future(Profile(id, names(id)))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Future(Profile(bfId, names(bfId)))
    }


  }
  // client: zuck to poke bi// here is the client applications
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck") // id already known
  mark.onComplete(t=>t match {
    case Success(markProfile) => {
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete(t=>t match {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      })
    }
    case Failure(e) => e.printStackTrace()
  })

  Thread.sleep(1000)


  // functional composition of futures
  // map, flatMap, filter

  val nameOnTheWall:Future[String] = mark.map(profile => profile.name)
  val markBestFriend:Future[Profile] = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zuckBestFriendRestricted = markBestFriend.filter(profile => profile.name.startsWith("2"))
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbacks

  // recovering

  val aProfileNoMatterWhat =SocialNetwork.fetchProfile("unknown id").recover({
    case e: Throwable => {// refer to the partial function
      println(e)
      Profile("fb.id.0-dummy", "Forever alone")// return a well define profile
    }
  })

  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith{
    case e: Throwable => {// refer to the partial function, this will call database again
      println(e) // fetch another profile
      SocialNetwork.fetchProfile("fb.id.0-dummy")
    }
  }

  // another pattern, falling back



}
