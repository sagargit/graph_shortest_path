
import com.google.inject.{Binder, Module}
import dao.{GraphDaoImpl, GraphDao}
import entity.{BaseEntityImpl, BaseEntity}

/**
 * Created by sagar on 5/29/16.
 */
class ServerContext extends Module {

  def configure(binder: Binder) = {
    binder.bind(classOf[BaseEntity]).to(classOf[BaseEntityImpl])
    binder.bind(classOf[GraphDao]).to(classOf[GraphDaoImpl])
  }

}