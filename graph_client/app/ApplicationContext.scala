
import com.google.inject.{Binder, Module}
import entity.{BaseEntityImpl, BaseEntity}
import service.{DataServiceImpl, DataService}

/**
 * Created by sagar on 5/29/16.
 */

class ApplicationContext extends Module {

  def configure(binder: Binder) = {
    binder.bind(classOf[BaseEntity]).to(classOf[BaseEntityImpl])
    binder.bind(classOf[DataService]).to(classOf[DataServiceImpl])
  }
}