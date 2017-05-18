package models

import play.api.libs.json.Json

/**
 * Created by sagar on 5/29/16.
 */
case class GraphStructure(
                           nodes: List[String],
                           edges: List[List[String]]
                           )

object GraphStructure {
  implicit val graphFormat = Json.format[GraphStructure]
}
