import com.google.inject.Guice
import controllers.Application
import play.api.{GlobalSettings, Logger}

/**
 * Created by sagar on 5/29/16.
 */
object Global extends GlobalSettings {


  private val injector = Guice.createInjector(new ApplicationContext)

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    injector.getInstance(controllerClass)
  }
}