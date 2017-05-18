package controllers

import javax.inject.Inject
import entity.BaseEntity
import forms.FormFactory
import models.{GraphStructure, Edge, Graph, Node}
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject()(baseEntity: BaseEntity)extends Controller {

  def index = Action {
    Ok(views.html.index("Create graph",FormFactory.noOfNodesAndEdgesForm))
  }

  def constructGraph = Action {
    implicit request =>
      FormFactory.noOfNodesAndEdgesForm.bindFromRequest.fold(
        error =>{
          BadRequest(views.html.index("Home page ",error))
        },
          data =>{
            Ok(views.html.create(FormFactory.graphForm,data.noOfNodes.getOrElse(0L),data.noOfEdges.getOrElse(0L),List.empty,List.empty,None))
          }
      )
  }

  def constructGraphForEdges = Action {
    implicit request =>
      FormFactory.noOfNodesAndEdgesForm.bindFromRequest.fold(
        error =>{
          BadRequest(views.html.index("Home Page",error))
        },
        data =>{
          Ok(views.html.create(FormFactory.graphForm,data.noOfNodes.getOrElse(0L),0l,List.empty,List.empty,None))
        }
      )
  }


  def createGraph(noOfNodes: String, noOfEdges: String, graphId: String ="") = Action.async{
    implicit request =>
    FormFactory.graphForm.bindFromRequest.fold(
    hasErrors => {
      Future.successful(BadRequest(views.html.create(hasErrors,Integer.parseInt(noOfNodes),Integer.parseInt(noOfEdges),List.empty,List.empty,None)))
    },
    hasData => {
      val numberOfNodes = Integer.parseInt(noOfNodes)
      val numberOfEdges = Integer.parseInt(noOfEdges)

    //  if(hasData.edges.isEmpty){

        val listOfNodes = hasData.nodes
        baseEntity.saveGraph(hasData).map{
          k =>
            k.fold(
            errors => {
              Ok(views.html.errors("Something went wrong",errors))
            },
            data => {
              Ok(views.html.list("New Graph Created !!!",data))
             // Ok(views.html.create(FormFactory.graphForm.fill(data),numberOfNodes,numberOfEdges,listOfNodes,List.empty,data._id))
            }
            )
        }

  /*    }
      else{
        val listOfNodes = hasData.nodes
        val listOfEdges = hasData.edges.get
        baseEntity.updateGraph(graphId,hasData).map{
              k =>
                k.fold(
                  errors => {
                    Ok(views.html.errors("Something went wrong",errors))
                  },
                  data => {
                    Ok(views.html.list("New Graph Created !!!",data))
                  }
                )
            }



      }*/
    }
    )
  }

  def showShortestPathForm(graphId: String) = Action.async {
    implicit request =>
      baseEntity.findGraphById(graphId).map{
        k =>
          k.fold(
          errors => {
            Ok(views.html.errors("Something went wrong",errors))
          },
          data => {
            val listOfNodes:List[Node] = data.nodes
           Ok(views.html.shortestPath("Compute Shortest Path",FormFactory.shortestPath,graphId,listOfNodes,None))
          }
          )
      }

  }

  def findShortestPath(graphId: String) = Action.async{
    implicit request =>
      FormFactory.shortestPath.bindFromRequest.fold(
      errors => {
               Future.successful(BadRequest(views.html.shortestPath("Compute Shortest Path",errors,graphId,List.empty,None)))
      },
      data => {
        val sourceNodeLabel = data.fromNode.getOrElse("")
        val destinationNodeLabel = data.toNode.getOrElse("")

        baseEntity.computeShortestPath(graphId,sourceNodeLabel,destinationNodeLabel).map{
          k =>
            k.fold(
            errors => {
              Ok(views.html.errors("Something went wrong",errors))
            },
            data => {
              if(data.nodes.isEmpty && data.totalCost ==0){
                Ok(views.html.shortestPath("Compute Shortest Path",FormFactory.shortestPath,graphId,List.empty,Some("The target node is not reachable !!!")))
              }
              else{
                val cost = data.totalCost
                val shortestPathResult ="The shortest path is: "+ data.nodes.foldLeft("")((r,c) => r + "->"+c.label) + s". The cost of shortest path is $cost"
                Ok(views.html.shortestPath("Compute Shortest Path",FormFactory.shortestPath,graphId,List.empty,Some(shortestPathResult)))
              }

            }
            )
        }
      }
      )

  }

  def viewAllGraphs = Action.async{
    implicit request =>
      baseEntity.findAllGraphs.map{
        k =>
          k.fold(
            errors => {
              Ok(views.html.errors("Something went wrong",errors))
            },
            data => {
              Ok(views.html.list("List all Graphs",data.toList))
            }
          )
      }
  }

  def showEditNodeForm(graphId: String, nodeId: String) = Action.async{
    implicit request =>
      baseEntity.findGraphById(graphId).map{
        k =>
          k.fold(
            errors => {
              Ok(views.html.errors("Something went wrong",errors))
            },
            data => {
              val listOfNodes:List[Node] = data.nodes
              val node = data.nodes.find(k => k.nodeId.getOrElse("").equalsIgnoreCase(nodeId)).get
              Ok(views.html.editOrAddNode("Edit Node ",FormFactory.nodeForm.fill(node),graphId,Some(nodeId),listOfNodes))
            }
          )
      }
  }

  def showEditEdgeForm(graphId: String, edgeId: String) = Action.async{
    implicit request =>

      baseEntity.findGraphById(graphId).map{
        k =>
          k.fold(
            errors => {
              Ok(views.html.errors("Something went wrong",errors))
            },
            data => {
              val listOfNodes:List[Node] = data.nodes
              val edge = data.edges.get.find(k => k.edgeId.getOrElse("").equalsIgnoreCase(edgeId)).get
              Ok(views.html.editOrAddEdge("Edit Edge ",FormFactory.edgeForm.fill(edge),graphId,Some(edgeId),listOfNodes))
            }
          )
      }
  }

  def showAddNodeForm(graphId: String) = Action{
    implicit request =>
      val listOfNodes: List[Node] = List.empty
      Ok(views.html.editOrAddNode("Add Node ",FormFactory.nodeForm,graphId,None,listOfNodes))
  }

  def showAddEdgeForm(graphId: String) = Action.async{
    implicit request =>
      baseEntity.findGraphById(graphId).map{
        k =>
          k.fold(
          errors => {
            Ok(views.html.errors("Something went wrong",errors))

          },
          data => {
            Ok(views.html.editOrAddEdge("Add Edge ",FormFactory.edgeForm,graphId,None,data.nodes))
          }
          )
      }


  }

  def updateNode(graphId: String) = Action.async{
    implicit request =>
      FormFactory.nodeForm.bindFromRequest.fold(
      errors =>{
        Future.successful(BadRequest(views.html.editOrAddNode("Edit Node ",errors,graphId,Some(""),List.empty)))
      },
      data =>{

        baseEntity.updateNode(data.nodeId.get,graphId,data).map{
           k=>
          k.fold(
          errors => {
            Ok(views.html.errors("Something went wrong",errors))
          },
          data => {

            val listOfGraphs: List[Graph] = data
            Ok(views.html.list("List all Graphs",listOfGraphs))
          }
          )
        }

      }
      )
  }

  def updateEdge(graphId: String) = Action.async{
    implicit request =>

      FormFactory.edgeForm.bindFromRequest.fold(
        errors =>{
          Future.successful(BadRequest(views.html.editOrAddEdge("Edit Edge ",errors,graphId,Some(""),List.empty)))
        },
        data =>{

          baseEntity.updateEdge(data.edgeId.get,graphId,data).map{
            k=>
              k.fold(
                errors => {
                  Ok(views.html.errors("Something went wrong",errors))
                },
                data => {

                  val listOfGraphs: List[Graph] = data
                  Ok(views.html.list("List all Graphs",listOfGraphs))
                }
              )
          }
        }
      )
  }

  def addNode(graphId: String) = Action.async{
    implicit request =>
      FormFactory.nodeForm.bindFromRequest.fold(
        errors =>{
          Future.successful(BadRequest(views.html.editOrAddNode("Add Node ",errors,graphId,None,List.empty)))
        },
        data =>{
          baseEntity.addNode(graphId,data).map{
            k=>
              k.fold(
                errors => {
                  Ok(views.html.errors("Something went wrong",errors))
                },
                data => {

                  val listOfGraphs: List[Graph] = data
                  Ok(views.html.list("List all Graphs",listOfGraphs))
                }
              )
          }
        }
      )
  }

  def addEdge(graphId: String) = Action.async{
    implicit request =>
      // fetch all nodes also
      FormFactory.edgeForm.bindFromRequest.fold(
        errors =>{
          Future.successful(BadRequest(views.html.editOrAddEdge("Add Edge ",errors,graphId,None,List.empty)))
        },
        data =>{
          baseEntity.addEdge(graphId,data).map{
            k=>
              k.fold(
                errors => {
                  Ok(views.html.errors("Something went wrong",errors))
                },
                data => {

                  val listOfGraphs: List[Graph] = data
                  Ok(views.html.list("List all Graphs",listOfGraphs))
                }
              )
          }
        }
      )
  }

  def deleteEdge(graphIdAndEdgeId: String) = Action.async{
    implicit request =>
      val idTuple = graphIdAndEdgeId.split(" ")
      val graphId = idTuple(0)
      val edgeId = idTuple(1)
      baseEntity.deleteEdge(edgeId,graphId).map{
        k=>
          k.fold(
            errors => {
              Ok(views.html.errors("Something went wrong",errors))
            },
            data => {

              val listOfGraphs: List[Graph] = data
              Ok(views.html.list("List all Graphs",listOfGraphs))
            }
          )
      }
  }

  def deleteNode(graphIdAndNodeId: String) = Action.async{
    implicit request =>
      val idTuple = graphIdAndNodeId.split(" ")
      val graphId = idTuple(0)
      val nodeId = idTuple(1)
      baseEntity.deleteNode(nodeId,graphId).map{
        k=>
          k.fold(
            errors => {
              Ok(views.html.errors("Something went wrong",errors))
            },
            data => {

              val listOfGraphs: List[Graph] = data
              Ok(views.html.list("List all Graphs",listOfGraphs))
            }
          )
      }
  }

  def deleteGraph(graphId: String) = Action.async{
    implicit request =>
      baseEntity.deleteGraph(graphId).map{
        k=>
          k.fold(
            errors => {
              Ok(views.html.errors("Something went wrong",errors))
            },
            data => {

              val listOfGraphs: List[Graph] = data
              Ok(views.html.list("List all Graphs",listOfGraphs))
            }
          )
      }
  }

  def viewGraph(graphId: String) = Action.async{
    implicit request =>
      baseEntity.findGraphById(graphId).map{
        k =>
          k.fold(
            errors => {
              Ok(views.html.errors("Something went wrong",errors))
            },
            data => {
              val arrayOfNodeLabels = data.nodes.map(k => k.label)
              var arrayOfEdges: List[List[String]] = List.empty
              if(data.edges.isDefined){
                 arrayOfEdges = data.edges.get.map(l => List(l.fromNode,l.toNode,"test Value"))
              }
              val graphStructure = GraphStructure(arrayOfNodeLabels,arrayOfEdges)

              val json: String = Json.stringify(GraphStructure.graphFormat.writes(graphStructure))
              Ok(views.html.display("View Graph",json))
            }
          )
      }

  }

}