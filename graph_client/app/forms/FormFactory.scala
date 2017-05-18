package forms

import models._
import play.api.data.{Forms, Form}
import play.api.data.Forms._

/**
 * Created by sagar on 5/28/16.
 */

object FormFactory {

  val graphForm: Form[Graph] = Form(
    mapping(
      "_id" -> optional(text),
      "name" -> optional(text),
      "nodes" -> list(
        mapping(
          "nodeId" -> optional(text),
          "label" -> nonEmptyText,
          "nodeType" -> optional(text)
        )(Node.apply)(Node.unapply)
      ),
      "edges" -> optional(list(
        mapping(
          "edgeId" -> optional(text),
          "edgeType" -> optional(text),
          "fromNode" -> nonEmptyText,
          "toNode" -> nonEmptyText,
          "weight" -> number
        )(Edge.apply)(Edge.unapply)
      ))

    )(Graph.apply)(Graph.unapply)
  )

  val noOfNodesAndEdgesForm: Form[NoOfEdgesAndNodes] = Form(
    mapping(
      "noOfEdges" -> optional(longNumber),
      "noOfNodes" -> optional(longNumber)
    )(NoOfEdgesAndNodes.apply)(NoOfEdgesAndNodes.unapply)
  )

  val shortestPath: Form[ShortestPath] = Form(
    mapping(
      "fromNode" -> optional(text),
      "toNode" -> optional(text)
    )(ShortestPath.apply)(ShortestPath.unapply)
  )


  val nodeForm: Form[Node] = Form(
    mapping(
      "nodeId" -> optional(text),
      "label" -> nonEmptyText,
      "nodeType" -> optional(text)
    )(Node.apply)(Node.unapply)
  )

  val edgeForm: Form[Edge] = Form(
    mapping(
      "edgeId" -> optional(text),
      "edgeType" -> optional(text),
      "fromNode" -> nonEmptyText,
      "toNode" -> nonEmptyText,
      "weight" -> number
    )(Edge.apply)(Edge.unapply)
  )


}
