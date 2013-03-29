package controllers

import play.api._
import play.api.mvc._


object Application extends Controller with SecurityTrait{

  def index = isAuthenticated{ username => implicit request =>
    Logger.info("hello "+username)
    Redirect(routes.Products.list())
  }

}
