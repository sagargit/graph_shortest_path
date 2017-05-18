package entity

import models.{ShortestPathResult, Node, Edge, Graph}
import models.Graph._
import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by sagar on 5/29/16.
 */

trait BaseEntity {

  def insert(graph: Graph)(implicit ctx: ExecutionContext):Future[List[Graph]]

  def getAllGraphs(implicit ctx: ExecutionContext) :Future[List[Graph]]

  def findById(id: String)(implicit ctx: ExecutionContext):Future[Option[Graph]]

  def updateForInsert(id: String,graph: Graph)(implicit ctx: ExecutionContext):Future[List[Graph]]

  def update(id: String,graph: Graph)(implicit ctx: ExecutionContext):Future[List[Graph]]

  def delete(id: String)(implicit ctx: ExecutionContext): Future[List[Graph]]

  def addEdge(graphId: String, edge: Edge)(implicit ctx: ExecutionContext): Future[List[Graph]]

  def updateNode(graphId: String,nodeId: String, node: Node)(implicit ctx: ExecutionContext): Future[List[Graph]]

  def updateEdge(graphId: String,edgeId: String, edge: Edge)(implicit ctx: ExecutionContext): Future[List[Graph]]

  def addNode(graphId: String, node: Node)(implicit ctx: ExecutionContext): Future[List[Graph]]

  def removeEdge(graphId: String, edgeId: String)(implicit ctx: ExecutionContext): Future[List[Graph]]

  def removeNode(graphId: String, nodeId: String)(implicit ctx: ExecutionContext): Future[List[Graph]]

  def findShortestPath(graphId: String, fromNodeLabel: String, toNodeLabel: String)(implicit ctx: ExecutionContext): Future[ShortestPathResult]


}
