package utils

import play.api.libs.json.Reads._
import play.api.libs.json._

/**
 * Created by sagar on 5/29/16.
 */
object MongoUtils {

  val writeId = (__.json.update((__ \ '_id).json.copyFrom((__ \ '_id \ '$oid).json.pick)))

  def transformMongoId(jsObject: JsObject): JsObject = {

    val jsObjectWithStringifiedId = (jsObject \ "_id") match {
      case s: JsString => jsObject
      case o: JsObject => jsObject.transform(writeId).get
      case _ => jsObject // no id present
    }
    jsObjectWithStringifiedId
  }

  val writeObjectId = (__.json.pickBranch((__ \ "_id").json.update(of[JsString].map { case JsString(id) => Json.obj("$oid" -> id) })))

  def transformIdToObjectId(jsObject: JsObject): JsObject = {
    jsObject \ "_id" match {
      case s: JsString => {
        jsObject.transform(writeObjectId).get
      }
      case i => {
        jsObject
      }
    }
  }

}
