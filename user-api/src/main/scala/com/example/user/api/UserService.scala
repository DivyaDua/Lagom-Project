package com.example.user.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait UserService extends Service {

  val TOPIC_NAME = "userdata"

  def greetUser(username: String): ServiceCall[NotUsed, String]

  def testUser(): ServiceCall[NotUsed, String]

  //def usersTopic(): Topic[UserDataChanged]

  override final def descriptor: Descriptor = {

    import Service._
    named("user")
      .withCalls(
        restCall(Method.GET, "/user-data/api", testUser _),
        restCall(Method.GET,"/user/api/:username", greetUser _)
      )
      .withAutoAcl(true)
    /*.withTopics(
      topic(TOPIC_NAME, usersTopic _)
    )*/
  }

}

case class UserData(userId: Int, id: Int, title:String, body: String)

object UserData {

  implicit val format: Format[UserData] = Json.format[UserData]

}

/*case class UserDataChanged(userId: Int, id: Int, title:String, body: String)

object UserDataChanged {

  implicit val format: Format[UserDataChanged] = Json.format[UserDataChanged]

}*/
