package model

import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Format
import play.api.libs.json.JsSuccess

case class Comment(val content: String, val documentBlockIdentifier: String) {

}

object Comment {

  implicit object CommentFormat extends Format[Comment] {
    def reads(json: JsValue) = JsSuccess(Comment(
      (json \ "comment").as[String],
      (json \ "documentBlockIdentifier").as[String]
    ))

    def writes(comment: Comment) = JsObject(Seq(
      "comment" -> JsString(comment.content),
      "documentBlockIdentifier" -> JsString(comment.documentBlockIdentifier)))
  }
}