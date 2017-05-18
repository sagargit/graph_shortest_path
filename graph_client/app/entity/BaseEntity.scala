package entity


import models.{Node, Edge, ShortestPathResult, Graph}
import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by sagar on 5/29/16.
 */

trait BaseEntity {

  def saveGraph(graph: Graph)(implicit ctx: ExecutionContext): Future[Either[String,List[Graph]]]

  def computeShortestPath(graphId: String, fromNodeLabel: String, toNodeLabel: String)(implicit ctx: ExecutionContext): Future[Either[String,ShortestPathResult]]

  def findGraphById(graphId: String)(implicit ctx: ExecutionContext): Future[Either[String, Graph]]

  def deleteGraph(graphId: String)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]]

  def updateGraph(graphId: String,graph: Graph)(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]]

  def findAllGraphs(implicit ctx: ExecutionContext): Future[Either[String, List[Graph]]]

  def updateEdge(edgeId: String, graphId: String, edge: Edge)(implicit ctx: ExecutionContext): Future[Either[String,List[Graph]]]

  def updateNode(nodeId: String, graphId: String, node: Node)(implicit ctx: ExecutionContext): Future[Either[String,List[Graph]]]

  def deleteEdge(edgeId: String, graphId: String)(implicit ctx: ExecutionContext):Future[Either[String, List[Graph]]]

  def deleteNode(nodeId: String, graphId: String)(implicit ctx: ExecutionContext):Future[Either[String, List[Graph]]]

  def addNode(graphId: String, node: Node)(implicit ctx: ExecutionContext): Future[Either[String,List[Graph]]]

  def addEdge(graphId: String, edge: Edge)(implicit ctx: ExecutionContext): Future[Either[String,List[Graph]]]

}
