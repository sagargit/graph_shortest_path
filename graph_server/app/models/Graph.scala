package models

import play.api.libs.json.{Writes, Json}
import reactivemongo.bson.BSONObjectID
import utils.Util._

/**
 * Created by sagar on 5/27/16.
 */

case class Graph(_id: Option[String] = Some(BSONObjectID.generate.stringify),
                 name: Option[String] = None,
                 nodes: List[Node] = List.empty,
                 edges: Option[List[Edge]] = Some(List.empty)) {

  def addNode(node: Node): Graph = {
    val updatedNode = node.copy(nodeId = Some(generateId))
    this.copy(nodes = this.nodes.+:(updatedNode))
  }

  def generateId = BSONObjectID.generate.stringify

  def feedIdToNodesAndEdges() = {
    if (this.edges.isDefined) {
      val listOfEdges = this.edges.get.map(k => k.copy(edgeId = Some(generateId)))
      val listOfNodes = this.nodes.map(l => l.copy(nodeId = Some(generateId)))
      this.copy(nodes = listOfNodes, edges = Some(listOfEdges))
    }
    else {
      val listOfNodes = this.nodes.map(l => l.copy(nodeId = Some(generateId)))
      this.copy(nodes = listOfNodes)
    }
  }

  def feedIdToEdges() = {
    val listOfEdges = this.edges.get.map(k => k.copy(edgeId = Some(generateId)))
    this.copy(edges = Some(listOfEdges))
  }

  def addEdge(edge: Edge): Graph = {
    if (this.edges.isDefined) {
      val updatedEdge = edge.copy(edgeId = Some(generateId))
      this.copy(edges = Some(this.edges.get.+:(updatedEdge)))
    }
    else {
      val updatedEdge = edge.copy(edgeId = Some(generateId))
      this.copy(edges = Some(List(updatedEdge)))
    }

  }

  def updateNode(nodeId: String, node: Node): Graph = {
    val nodeToBeUpdated = this.nodes.find(k => k.nodeId.get.equalsIgnoreCase(nodeId)).get
    val newListOfNodes = remove(nodeToBeUpdated, this.nodes)
    this.copy(nodes = newListOfNodes.+:(node))
  }

  def updateEdge(edgeId: String, edge: Edge): Graph = {
    val edgeToBeUpdated = this.edges.get.find(k => k.edgeId.get.equalsIgnoreCase(edgeId)).get
    val newListOfEdges = remove(edgeToBeUpdated, this.edges.get)
    this.copy(edges = Some(newListOfEdges.+:(edge)))
  }

  def minusEdge(edge: Edge): Graph = {
    /*  if (isConnectedToALeafNode(edge)) {
        val remainingListOfEdges = this.edges.get.filter(l => !l.edgeId.get.equalsIgnoreCase(edge.edgeId.get))

        val resultFirst: Boolean = remainingListOfEdges.exists(k => k.fromNode.equalsIgnoreCase(edge.fromNode) || k.toNode.equalsIgnoreCase(edge.fromNode))
        val resultSecond: Boolean = remainingListOfEdges.exists(k => k.toNode.equalsIgnoreCase(edge.fromNode) || k.toNode.equalsIgnoreCase(edge.toNode))

        if (resultFirst) {
          this.copy(nodes = remove(this.nodes.find(k => k.nodeId.get.equalsIgnoreCase(edge.fromNode)).get, this.nodes),
            edges = Some(remove(edge, this.edges.get)))
        } else {
          this.copy(nodes = remove(this.nodes.find(k => k.nodeId.get.equalsIgnoreCase(edge.toNode)).get, this.nodes),
            edges = Some(remove(edge, this.edges.get)))
        }

      }
      else {*/
    this.copy(edges = Some(remove(edge, this.edges.get)))
    //    }
  }

  def minusAtomicNode(node: Node): Graph = {
    this.copy(nodes = remove(node, this.nodes))
  }

  def minusNode(node: Node): Graph = {
    val edgesConnected = edgesConnectedToGivenNode(node)
    if (edgesConnected.length > 0) {
      this.copy(edges = Some(this.edges.get.filter(edge => !edgesConnected.exists(l => l.edgeId.get.equalsIgnoreCase(edge.edgeId.get)))),
        nodes = minusAtomicNode(node).nodes)
    }
    else {
      this.copy(nodes = minusAtomicNode(node).nodes)
    }

  }

  def edgesConnectedToGivenNode(node: Node): List[Edge] = {
    if (this.edges.isEmpty) {
      List.empty
    }
    else {
      this.edges.get.filter(k => k.fromNode.equalsIgnoreCase(node.label) || k.toNode.equalsIgnoreCase(node.label))
    }
  }

  def isConnectedToALeafNode(edge: Edge): Boolean = {
    val remainingListOfEdges = this.edges.get.filter(l => !l.edgeId.get.equalsIgnoreCase(edge.edgeId.get))

    !(remainingListOfEdges.exists(k => k.fromNode.equalsIgnoreCase(edge.fromNode) || k.toNode.equalsIgnoreCase(edge.fromNode)) ||
      remainingListOfEdges.exists(k => k.toNode.equalsIgnoreCase(edge.fromNode) || k.toNode.equalsIgnoreCase(edge.toNode)))
  }

  def shortestPath(source: Node, target: Node): Option[(List[Node], Int)] = {
    if (isReachable(source, target)) {
      val dijkstraResult = dijkstra(this)(source)
      val shortestPathCost = dijkstraResult._1(target)
      val pred = dijkstraResult._2
      if (pred.contains(target) || source.equalsTo(target)) {
        val result = iterateRightMap(target)(pred)
        Some(result, shortestPathCost)

      }
      else {
        None
      }
    }
    else {
      Some((List.empty, 0))
    }


  }

  def isReachable(source: Node, target: Node): Boolean = {
    val dijkstraResult = dijkstra(this)(source)
    dijkstraResult._1.get(target) match {
      case Some(cost) => true

      case None => false
    }
  }

}

object Graph {

  import Node._
  import Edge._

  implicit val format = Json.format[Graph]
  implicit val reads = Json.reads[Graph]
  implicit val graphWrites = Json.writes[Graph]
  implicit val listOfGraphWrites = Writes.seq(graphWrites)
}






