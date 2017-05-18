package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
 * Created by sagar on 5/27/16.
 */

case class Node(nodeId: Option[String] = Some(BSONObjectID.generate.stringify),
                label: String,
                nodeType: Option[String] = None) {
  def equalsTo(that: Node): Boolean = {
    this.nodeId.get.equalsIgnoreCase(that.nodeId.get)
  }

}

object Node {
  implicit val format = Json.format[Node]
}

