package dao

import models.Graph
import play.api.libs.json.{Json, JsObject}
import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by sagar on 5/29/16.
 */

trait GraphDao {

  def insert(graph: Graph)(implicit ctx: ExecutionContext): Future[Graph]

  def find(implicit ctx: ExecutionContext): Future[List[JsObject]]

  def findById(id: String)(implicit ctx: ExecutionContext): Future[Option[JsObject]]

  def findOne(t: JsObject)(implicit ctx: ExecutionContext): Future[Option[JsObject]]

  def update(id: String, graph: Graph)(implicit ctx: ExecutionContext): Future[Unit]

  def delete(id: String)(implicit ctx: ExecutionContext): Future[Unit]

}
