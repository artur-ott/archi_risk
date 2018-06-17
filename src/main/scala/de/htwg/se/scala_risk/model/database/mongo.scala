package de.htwg.se.scala_risk.model.database

object mongo {
  import org.mongodb.scala.{MongoClient, MongoDatabase}
  import org.mongodb.scala.{MongoCollection, Document}
  import scala.concurrent.Await
  import scala.concurrent.duration.Duration
  private[this] val mongoClient: MongoClient = MongoClient("mongodb://127.0.0.1")
  private[model] implicit val MONGO_DB: MongoDatabase = mongoClient.getDatabase("archi")

  private implicit val GAME: MongoCollection[Document] = MONGO_DB.getCollection("game")

  var id = -1
  var break = false

  def saveToMongoDB(s: String) = {

    print(id + "before")
    if (id == -1) {
      while (!break) {
        id += 1
        if (Await.result(GAME.count(Document(
          "id" -> id
        )).toFuture, Duration.Inf).asInstanceOf[Int] == 0) {
          Await.result(GAME.insertOne(Document(
            "id" -> id,
            "data" -> ""
          )).toFuture, Duration.Inf)
          break = true
        }
      }
    }

    Await.result(GAME.updateOne(
      Document(
        "id" -> id
      ),
      Document(
        "$set" -> Document(
          "id" -> id,
          "data" -> s
        )
      )
    ).toFuture, Duration.Inf)
    id+=1
    print(id + "after")
  }

  def bar(s: String) = {

    Await.result(GAME.find(Document(
      "data" -> s
    )).toFuture, Duration.Inf)

  }

  def loadFromMongoDB(id: Int) : String = {

    Await.result(GAME.find().collect().toFuture(), Duration.Inf).toString()

  }



}
