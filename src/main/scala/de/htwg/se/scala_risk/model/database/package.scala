package de.htwg.se.scala_risk.model

package object database {
  import org.mongodb.scala.{MongoClient, MongoDatabase}
  import org.mongodb.scala.{MongoCollection, Document}
  import scala.concurrent.Await
  import scala.concurrent.duration.Duration
  private[this] val mongoClient: MongoClient = MongoClient("mongodb://127.0.0.1")
  private[model] implicit val MONGO_DB: MongoDatabase = mongoClient.getDatabase("archi")

  private implicit val CLIENT: MongoCollection[Document] = MONGO_DB.getCollection("client")

  Await.result(CLIENT.insertOne(Document(
    "Name" -> "admin",
    "Name1" -> "admin2",
  )).toFuture, Duration.Inf)
}
