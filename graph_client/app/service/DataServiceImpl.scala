package service

import dispatch._
import play.api.libs.json.{Json, JsValue}
import scala.concurrent.ExecutionContext

/**
 * Created by sagar on 5/29/16.
 */

class DataServiceImpl extends DataService {

  val host = "http://localhost:9001/graphs"

  def createGraph(json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val jsonBody = Json.stringify(json)
    val request = url(host)
    val req = request.POST.setBody(jsonBody).setHeader("Content-Type", "application/json")
    Http(req OK as.String).either
  }

  def getGraphById(graphId: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val request = url(host + s"/$graphId")
    val req = request.GET
    val result = Http(req OK as.String).either
    result
  }

  def deleteGraph(graphId: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val request = url(host + s"/$graphId")
    val req = request.DELETE
    val result = Http(req OK as.String).either
    result
  }

  def getAllGraphs(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val request = url(host)
    val req = request.GET
    val result = Http(req OK as.String).either
    result
  }

  def updateGraph(graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val jsonBody = Json.stringify(json)
    val request = url(host + s"/$graphId")
    val req = request.PUT.setBody(jsonBody).setHeader("Content-Type", "application/json")
    val result = Http(req OK as.String).either
    result
  }

  def updateEdge(edgeId: String, graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val jsonBody = Json.stringify(json)
    val request = url(host + s"/$graphId" + s"/$edgeId" + "/edge")
    val req = request.PUT.setBody(jsonBody).setHeader("Content-Type", "application/json")
    Http(req OK as.String).either
  }

  def updateNode(nodeId: String, graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val jsonBody = Json.stringify(json)
    val request = url(host + s"/$graphId" + s"/$nodeId" + "/node")
    val req = request.PUT.setBody(jsonBody).setHeader("Content-Type", "application/json")
    Http(req OK as.String).either
  }

  def deleteEdge(edgeId: String, graphId: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val request = url(host + s"/$graphId" + s"/$edgeId" + "/edge")
    val req = request.DELETE
    Http(req OK as.String).either
  }

  def deleteNode(nodeId: String, graphId: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val request = url(host + s"/$graphId" + "/node" + s"/$nodeId")
    val req = request.DELETE
    Http(req OK as.String).either
  }

  def addNode(graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val jsonBody = Json.stringify(json)
    val request = url(host + s"/$graphId" + s"/node")
    val req = request.POST.setBody(jsonBody).setHeader("Content-Type", "application/json")
    Http(req OK as.String).either
  }

  def addEdge(graphId: String, json: JsValue)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val jsonBody = Json.stringify(json)
    val request = url(host + s"/$graphId" + s"/edge")
    val req = request.POST.setBody(jsonBody).setHeader("Content-Type", "application/json")
    Http(req OK as.String).either
  }

  def findShortestPath(graphId: String, fromNodeLabel: String, toNodeLabel: String)(implicit ctx: ExecutionContext): Future[Either[Throwable, String]] = {
    val request = url(host + s"/$graphId" + s"/$fromNodeLabel" + s"/$toNodeLabel" + "/shortestPath")
    val req = request.GET
    val result = Http(req OK as.String).either
    result
  }


}
