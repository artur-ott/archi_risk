package de.htwg.se.scala_risk.model.database

import javax.inject.Singleton

@Singleton
object mongo {
  import org.mongodb.scala.{MongoClient, MongoDatabase}
  import org.mongodb.scala.{MongoCollection, Document}
  import scala.concurrent.Await
  import scala.concurrent.duration.Duration
  import org.mongodb.scala._
  import org.mongodb.scala.model.Sorts._

  private[this] val mongoClient: MongoClient = MongoClient("mongodb://127.0.0.1")
  private[model] implicit val MONGO_DB: MongoDatabase = mongoClient.getDatabase("archi")
  private implicit val GAME: MongoCollection[Document] = MONGO_DB.getCollection("game")

  def saveToMongoDB(id: Int, s: String) = {

    Await.result(GAME.insertOne(Document(
      "id" -> id,
      "data" -> s
    )).toFuture, Duration.Inf)
  }

  // Always load the latest data from db
  def loadFromMongoDB() : String = {

    val res = Await.result(GAME.find().sort(descending("id")).first().toFuture(), Duration.Inf)
    res.get("data").get.asString().getValue

  }



}
