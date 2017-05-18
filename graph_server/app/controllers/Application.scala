package controllers

import javax.inject.Inject
import entity.BaseEntity
import models.{Node, Edge, Graph}
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import Graph._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import models.ShortestPathResult._

class Application @Inject()(baseEntity: BaseEntity) extends Controller {

  def createGraph = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Graph].map {
        k =>
          baseEntity.insert(k).map(
            l => Ok(listOfGraphWrites.writes(l))
          )
      }.getOrElse(Future.successful(BadRequest(Json.obj("msg" -> "Something went wrong"))))

  }

  def getAllGraphs = Action.async {
    implicit request =>
      baseEntity.getAllGraphs.map {
        l => Ok(listOfGraphWrites.writes(l))
      }
  }

  def findGraphById(graphId: String) = Action.async {
    implicit request =>
      baseEntity.findById(graphId).map {
        case Some(graph) =>
          Ok(graphWrites.writes(graph))
        case None =>
          BadRequest(Json.obj("msg" -> "Something went wrong"))
      }
  }

  def update(graphId: String) = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Graph].map {
        k =>
          baseEntity.updateForInsert(graphId, k).map(
            l => Ok(listOfGraphWrites.writes(l))
          )
      }.getOrElse(Future.successful(BadRequest(Json.obj("msg" -> "Something went wrong"))))
  }

  def delete(graphId: String) = Action.async {
    implicit request =>
      baseEntity.delete(graphId).map {
        k => Ok(listOfGraphWrites.writes(k))
      }
  }

  def addEdge(graphId: String) = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Edge].map {
        k =>
          baseEntity.addEdge(graphId, k).map {
            l => Ok(listOfGraphWrites.writes(l))
          }
      }.getOrElse(Future.successful(BadRequest(Json.obj("msg" -> "Something went wrong"))))
  }

  def addNode(graphId: String) = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Node].map {
        k =>
          baseEntity.addNode(graphId, k).map {
            l => Ok(listOfGraphWrites.writes(l))
          }
      }.getOrElse(Future.successful(BadRequest(Json.obj("msg" -> "Something went wrong"))))
  }

  def updateEdge(graphId: String, edgeId: String) = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Edge].map {
        k =>
          baseEntity.updateEdge(graphId, edgeId, k).map {
            l => Ok(listOfGraphWrites.writes(l))
          }
      }.getOrElse(Future.successful(BadRequest(Json.obj("msg" -> "Something went wrong"))))
  }

  def updateNode(graphId: String, nodeId: String) = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Node].map {
        k =>

          baseEntity.updateNode(graphId, nodeId, k).map {
            l => Ok(listOfGraphWrites.writes(l))
          }
      }.getOrElse(Future.successful(BadRequest(Json.obj("msg" -> "Something went wrong"))))
  }

  def deleteEdge(graphId: String, edgeId: String) = Action.async {
    implicit request =>
      baseEntity.removeEdge(graphId, edgeId).map {
        l => Ok(listOfGraphWrites.writes(l))
      }
  }

  def deleteNode(graphId: String, nodeId: String) = Action.async {
    implicit request =>
      baseEntity.removeNode(graphId, nodeId).map {
        l => Ok(listOfGraphWrites.writes(l))
      }

  }

  def findShortestPath(graphId: String, fromNodeLabel: String, toNodeLabel: String) = Action.async {
    implicit request =>
      baseEntity.findShortestPath(graphId, fromNodeLabel, toNodeLabel).map {
        l => Ok(shortestPathFormat.writes(l))
      }
  }

}
