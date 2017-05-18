package models

import play.api.libs.json.Json

/**
 * Created by sagar on 5/29/16.
 */

case class ShortestPathResult(
                               nodes: List[Node],
                               totalCost: Int
                               )

object ShortestPathResult {
  implicit val shortestPathFormat = Json.format[ShortestPathResult]
}
