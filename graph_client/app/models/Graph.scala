package models

import play.api.libs.json.{Writes, Reads, Json}

/**
 * Created by sagar on 5/28/16.
 */

case class Graph(_id: Option[String] = None,
                 name: Option[String] = None,
                 nodes: List[Node],
                 edges: Option[List[Edge]] = Some(List.empty))

object Graph {

  import Node._
  import Edge._

  implicit val graphFormat = Json.format[Graph]
  implicit val graphReads = Json.reads[Graph]
  implicit val listGraphReads = Reads.seq(graphReads)

  implicit val graphWrites = Json.writes[Graph]
  implicit val listGraphWrites = Writes.seq(graphWrites)

}


