package utils

import models.{Node, Graph}
import scala.annotation.tailrec

/**
 * Created by sagar on 5/27/16.
 */

object Util {

  def iterateRightMap[A](x: A)(m:Map[A,A] ): List[A] = {
    @tailrec
    def go(x: A, acc: List[A]): List[A] = m.get(x) match {
      case None => x :: acc
      case Some(y) => go(y, x :: acc)
    }

    go(x, List.empty)
  }

  def remove[T](element: T, list: List[T]): List[T] = {
    list diff List(element)
  }

  def dijkstra(g: Graph)(source: Node): (Map[Node, Int], Map[Node, Node]) = {
    @tailrec
    def go(active: Set[Node], res: Map[Node, Int], pred: Map[Node, Node]): (Map[Node, Int], Map[Node, Node]) =
      if (active.isEmpty) (res, pred)
      else {
        val node = active.minBy(res)
        val cost = res(node)
        val neighbours = for {
          (n, c) <- graphFunction(node, g) if cost + c < res.getOrElse(n, Int.MaxValue)
        } yield n -> (cost + c)
        val preds = neighbours mapValues (_ => node)
        go(active - node ++ neighbours.keys, res ++ neighbours, pred ++ preds)
      }

    go(Set(source), Map(source -> 0), Map.empty)
  }

  def graphFunction(sourceNode: Node, g: Graph): Map[Node, Int] = {
    g.edges.get.foldLeft(Map[Node,Int]().empty) {
      (r, c) =>
        if (c.fromNode.equalsIgnoreCase(sourceNode.label)) {
          r.+((g.nodes.find(k => k.label.equalsIgnoreCase(c.toNode)).get, c.weight))
        }
        else {
          r
        }
    }
  }

}
