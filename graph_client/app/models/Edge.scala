package models

import play.api.libs.json.{Reads, Json}

/**
 * Created by sagar on 5/29/16.
 */

case class Edge(edgeId: Option[String] = None,
                edgeType: Option[String] = None,
                fromNode: String,
                toNode: String,
                weight: Int)

object Edge {
  /*  implicit val edgeRead = Json.reads[Edge]
    implicit val listEdgeReadFormat = Reads.seq(edgeRead)*/

  import Node._

  implicit val edgeFormat = Json.format[Edge]
}
