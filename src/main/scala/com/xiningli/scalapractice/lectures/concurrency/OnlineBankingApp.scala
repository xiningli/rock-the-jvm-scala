package com.xiningli.scalapractice.lectures.concurrency
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
object OnlineBankingApp extends App{


  case class User(name: String)
  case class Transaction(sender:String, receiver:String, amount: Double, status: String)
  object BankingApp extends {
    val name = "Rock the JVM banking"
    def fetchUser(name:String): Future[User] = Future((()=>{
      Thread.sleep(5000)
      User(name)
    })())

    def createTransaction(user:User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // simulate some process
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from the DB
      // create a transaction
      // wait for the transaction to finish
      val transactionStatusFuture = for {
        usr <- fetchUser(username)
        transaction <- createTransaction(usr, merchantName, cost)
      } yield transaction.status

      val fetchedUserFuture:Future[User] = fetchUser(username)

      val transactionStatusFuture2 = fetchedUserFuture.flatMap(createTransaction(_, merchantName, cost)).map(_.status)


      Await.result(transactionStatusFuture, 2.seconds)
    }

  }



}
