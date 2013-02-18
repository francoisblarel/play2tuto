package controllers

import play.api.mvc._
import play.api.mvc.Controller
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.Messages
import models.Product

/**
 * User: fblarel
 * Date: 13/02/13 
 */
object Products extends Controller{

  private val productForm : Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying("validation.ean.duplicate",Product.find(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  def list = Action{implicit request =>
    val products = Product.findAll
    Ok(views.html.products.list(products))
  }

  def show(ean : Long) = Action{implicit request =>
    Product.find(ean).map {product =>
      Ok(views.html.products.details(product))
    }.getOrElse(NotFound)
  }

  def save() = Action{ implicit request =>
    val newProductForm = this.productForm.bindFromRequest()
    newProductForm.fold(
      hasErrors = {form =>
        Redirect(routes.Products.newProduct()).
          flashing(Flash(form.data) +
          ("error" -> Messages("validation.errors")))
      },
      success = { newProduct =>
        Product.add(newProduct)
        Redirect(routes.Products.show(newProduct.ean)).
          flashing("success" -> Messages("products.new.success", newProduct.name))
      }
    )
  }

  def newProduct() = Action{implicit request =>
    val form = if (flash.get("error").isDefined)
      this.productForm.bind(flash.data)
    else
      this.productForm
    Ok(views.html.products.editProduct(form))
  }

}
