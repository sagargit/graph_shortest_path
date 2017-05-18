package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
 * Created by sagar on 5/27/16.
 */

case class Edge(edgeId: Option[String] = None,
                edgeType: Option[String] = None,
                fromNode: String, // unique
                toNode: String, // unique
                weight: Int)

object Edge {
  implicit val format = Json.format[Edge]
}
