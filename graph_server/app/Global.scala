import com.google.inject.Guice
import play.api.GlobalSettings

/**
 * Created by sagar on 5/29/16.
 */

object Global extends GlobalSettings {

  private val injector = Guice.createInjector(new ServerContext)

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    injector.getInstance(controllerClass)
  }
}
