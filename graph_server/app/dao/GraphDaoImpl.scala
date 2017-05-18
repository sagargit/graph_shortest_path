package dao

import models.Graph
import play.api.libs.json.{Json, JsObject, Writes, Reads}
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.Play.current
import reactivemongo.bson.BSONObjectID
import utils.MongoUtils
import scala.concurrent.{ExecutionContext, Future}
import play.modules.reactivemongo.json.BSONFormats
import play.modules.reactivemongo.json.BSONFormats._

/**
 * Created by sagar on 5/29/16.
 */

class GraphDaoImpl extends GraphDao {

  val reader: Reads[Graph] = implicitly[Reads[Graph]]
  val writer: Writes[Graph] = implicitly[Writes[Graph]]

  def db = ReactiveMongoPlugin.db

  val collection: JSONCollection = db[JSONCollection]("graphs")

  def appendObjId(t: JsObject): JsObject = {
    val id = BSONObjectID.generate
    t ++ Json.obj("_id" -> id)
  }

  def insert(graph: Graph)(implicit ctx: ExecutionContext): Future[Graph] = {

    val modelToJsObj = writer.writes(graph).as[JsObject]
    val objectWithId = appendObjId(modelToJsObj)
    collection.insert(modelToJsObj) map (_ => reader.reads(MongoUtils.transformMongoId(objectWithId)).get)
  }

  def find(implicit ctx: ExecutionContext): Future[List[JsObject]] = {
    val filter = Json.obj()
    collection.find(filter).cursor[JsObject].collect[List]().map {
      list =>
        if (list.size > 0)
          list.map(k => MongoUtils.transformMongoId(k))
        else
          List[JsObject]()
    }
  }

  def findById(id: String)(implicit ctx: ExecutionContext): Future[Option[JsObject]] = {
    val filter = Json.obj("_id" -> Json.obj("$oid" -> id))
    collection.find(filter).cursor[JsObject].headOption.map {
      case Some(k) => Some(MongoUtils.transformMongoId(k))

      case None => None
    }
  }

  def findOne(t: JsObject)(implicit ctx: ExecutionContext): Future[Option[JsObject]] = {
    collection.find(t).cursor[JsObject].headOption
  }

  def update(id: String, graph: Graph)(implicit ctx: ExecutionContext): Future[Unit] = {
    val idObject = Json.obj("_id" -> Json.obj("$oid" -> id))
    val modelToJsObj = writer.writes(graph.copy(_id = None)).as[JsObject]
    Future.successful(collection.uncheckedUpdate(idObject, Json.obj("$set" -> modelToJsObj)))
  }

  def delete(id: String)(implicit ctx: ExecutionContext): Future[Unit] = {
    collection.remove(Json.obj("_id" -> BSONObjectID.parse(id).get)).map(_ => ())
  }
}