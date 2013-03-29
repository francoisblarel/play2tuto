package controllers

import play.api._
import play.api.mvc._


object Application extends Controller with SecurityTrait{

  private val loginForm : Form[User] = Form(
    mapping(
        "login" -> nonEmptyText,
        "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )

  def index = isAuthenticated{ username => implicit request =>
    Logger.info("hello "+username)
    Redirect(routes.Products.list())
  }

}
