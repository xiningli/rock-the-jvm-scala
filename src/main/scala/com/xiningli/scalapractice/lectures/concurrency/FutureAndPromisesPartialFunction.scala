package com.xiningli.scalapractice.lectures.concurrency

import com.xiningli.scalapractice.lectures.concurrency.FutureAndPromises.Profile

import scala.concurrent.Future
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object FutureAndPromisesPartialFunction extends App{
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
  println("printing a the retry db approach" + aFetchedProfileNoMatterWhat)
  println("printing a valid profile approach" + aProfileNoMatterWhat)
  aFetchedProfileNoMatterWhat.onComplete({
    case Success(m) => println("I am done with no exception")
    case Failure(e) => println("fail with exception: " + e.getMessage() + " self with " + aFetchedProfileNoMatterWhat)
  })
  aProfileNoMatterWhat.onComplete(x=>x match {
    case Success(s) => println("success with: "+s)
    case Failure(e) => println(e.getMessage)
  })
  Thread.sleep(2000)
}
