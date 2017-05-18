package entity

import com.google.inject.Inject
import dao.GraphDao
import models.{ShortestPathResult, Node, Edge, Graph}
import Graph._
import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by sagar on 5/29/16.
 */

class BaseEntityImpl @Inject()(graphDao: GraphDao) extends BaseEntity {

  def insert(graph: Graph)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    val updatedGraph = graph.feedIdToNodesAndEdges()
    graphDao.insert(updatedGraph).flatMap {
      k => getAllGraphs
    }
  }

  def getAllGraphs(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    graphDao.find.map {
      listOfJs =>
        listOfJs.map(k => reads.reads(k).get)

    }
  }

  def findById(id: String)(implicit ctx: ExecutionContext): Future[Option[Graph]] = {
    graphDao.findById(id).map {
      case Some(x) =>
        Some(reads.reads(x).get)

      case None => None
    }
  }

  def updateForInsert(id: String, graph: Graph)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    val updatedGraph = graph.feedIdToEdges()
    graphDao.update(id, updatedGraph).flatMap {
      k =>
        getAllGraphs
    }
  }

  def update(id: String, graph: Graph)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    graphDao.update(id, graph).flatMap {
      k =>
        getAllGraphs
    }
  }

  def delete(id: String)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    graphDao.delete(id).flatMap {
      k =>
        getAllGraphs
    }
  }

  def addEdge(graphId: String, edge: Edge)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    findById(graphId).flatMap {
      case Some(graph) =>
        val newGraph = graph.addEdge(edge)
        update(graphId, newGraph).flatMap {
          l =>
            getAllGraphs
        }


      case None => Future.successful(List.empty)
    }
  }

  def updateNode(graphId: String, nodeId: String, node: Node)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    findById(graphId).flatMap {
      case Some(graph) =>
        val newGraph = graph.updateNode(nodeId, node)
        update(graphId, newGraph).flatMap {
          l =>
            getAllGraphs
        }


      case None => Future.successful(List.empty)
    }
  }

  def updateEdge(graphId: String, edgeId: String, edge: Edge)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    findById(graphId).flatMap {
      case Some(graph) =>
        val newGraph = graph.updateEdge(edgeId, edge)
        update(graphId, newGraph).flatMap {
          l =>
            getAllGraphs
        }


      case None => Future.successful(List.empty)
    }
  }

  def addNode(graphId: String, node: Node)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    findById(graphId).flatMap {
      case Some(graph) =>
        val newGraph = graph.addNode(node)
        update(graphId, newGraph).flatMap {
          l =>
            getAllGraphs
        }

      case None => Future.successful(List.empty)
    }
  }

  def removeEdge(graphId: String, edgeId: String)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    findById(graphId).flatMap {
      case Some(graph) =>
        val edgeToBeDeleted = graph.edges.get.find(k => k.edgeId.get.equalsIgnoreCase(edgeId)).get
        val newGraph = graph.minusEdge(edgeToBeDeleted)
        update(graphId, newGraph).flatMap {
          l =>
            getAllGraphs
        }
      case None => Future.successful(List.empty)
    }
  }

  def removeNode(graphId: String, nodeId: String)(implicit ctx: ExecutionContext): Future[List[Graph]] = {
    findById(graphId).flatMap {
      case Some(graph) =>
        val nodeToBeDeleted = graph.nodes.find(k => k.nodeId.get.equalsIgnoreCase(nodeId)).get
        val newGraph = graph.minusNode(nodeToBeDeleted)
        update(graphId, newGraph).flatMap {
          l =>
            getAllGraphs
        }
      case None => Future.successful(List.empty)
    }
  }

  def findShortestPath(graphId: String, fromNodeLabel: String, toNodeLabel: String)(implicit ctx: ExecutionContext): Future[ShortestPathResult] = {

    findById(graphId).flatMap {
      case Some(graph) =>
        val fromNode = graph.nodes.find(k => k.label.equalsIgnoreCase(fromNodeLabel)).get
        val toNode = graph.nodes.find(l => l.label.equalsIgnoreCase(toNodeLabel)).get
        val res = graph.shortestPath(fromNode, toNode)
        if (res.isDefined) {
          Future.successful(ShortestPathResult(res.get._1,res.get._2))
        }
        else {
          Future.successful(ShortestPathResult(List.empty,0))
        }

      case None => Future.successful(ShortestPathResult(List.empty,0))
    }
  }


}
