package models

import play.api.libs.json.Json

/**
 * Created by sagar on 5/29/16.
 */
case class Node(nodeId: Option[String] = None,
                label: String,
                nodeType: Option[String] = None)

object Node {
  implicit val nodeFormat = Json.format[Node]
}
