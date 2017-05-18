package entity


import javax.inject.Inject
import models.{Node, Edge, ShortestPathResult, Graph}
import play.api.libs.json.Json
import service.DataService
import models.Graph._
import ShortestPathResult._
import scala.concurrent.{ExecutionContext, Future}
import Edge._
import Node._

/**
 * Created by sagar on 5/29/16.
 */

class BaseEntityImpl @Inject()(dataService: DataService) extends BaseEntity {

  def saveGraph(graph: Graph)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {
    val json = graphFormat.writes(graph)
    dataService.createGraph(json).map {
      k =>
        k.fold(
          error => {
            Left(error.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }
  }

  def computeShortestPath(graphId: String, fromNodeLabel: String, toNodeLabel: String)(implicit ctx: ExecutionContext): Future[Either[String, ShortestPathResult]] = {
    dataService.findShortestPath(graphId, fromNodeLabel, toNodeLabel).map {
      k =>
        k.fold(
          error => {
            Left(error.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(shortestPathFormat.reads(jsonData).get)
          }
        )
    }
  }

  def findGraphById(graphId: String)(implicit ctx: ExecutionContext): Future[Either[String, Graph]] = {
    dataService.getGraphById(graphId).map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(graphFormat.reads(jsonData).get)
          }
        )

    }
  }

  def deleteGraph(graphId: String)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {

    dataService.deleteGraph(graphId).map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }

  }

  def updateGraph(graphId: String, graph: Graph)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {

    val jsonData = graphFormat.writes(graph)
    dataService.updateGraph(graphId, jsonData).map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }

  }

  def findAllGraphs(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {
    dataService.getAllGraphs.map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }
  }

  def updateEdge(edgeId: String, graphId: String, edge: Edge)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {

    val jsonData = edgeFormat.writes(edge)
    dataService.updateEdge(edgeId, graphId, jsonData).map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }

  }

  def updateNode(nodeId: String, graphId: String, node: Node)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {

    val jsonData = nodeFormat.writes(node)
    dataService.updateNode(nodeId, graphId, jsonData).map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }

  }

  def deleteEdge(edgeId: String, graphId: String)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {

    dataService.deleteEdge(edgeId, graphId).map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }
  }

  def deleteNode(nodeId: String, graphId: String)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {
    dataService.deleteNode(nodeId, graphId).map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }
  }

  def addNode(graphId: String, node: Node)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {

    val jsonData = nodeFormat.writes(node)
    dataService.addNode(graphId, jsonData).map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }

  }

  def addEdge(graphId: String, edge: Edge)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]] = {

    val jsonData = edgeFormat.writes(edge)
    dataService.addEdge(graphId, jsonData).map {
      k =>
        k.fold(
          errors => {
            Left(errors.getMessage)
          },
          data => {
            val jsonData = Json.parse(data)
            Right(listGraphReads.reads(jsonData).get.toList)
          }
        )
    }

  }

}
