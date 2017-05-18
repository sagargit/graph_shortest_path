package service

import dispatch._
import play.api.libs.json.{Json, JsValue}
import scala.concurrent.ExecutionContext

/**
 * Created by sagar on 5/29/16.
 */
trait DataService {

  def createGraph(json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def getGraphById(graphId: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def deleteGraph(graphId: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def getAllGraphs(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def updateGraph(graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def updateEdge(edgeId: String, graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def updateNode(nodeId: String, graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def deleteEdge(edgeId: String, graphId: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def deleteNode(nodeId: String, graphId: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def addNode(graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def addEdge(graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

  def findShortestPath(graphId: String, fromNodeLabel: String, toNodeLabel: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]]

}
