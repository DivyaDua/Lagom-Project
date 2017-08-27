package com.example.user.api

import akka.NotUsed
import com.example.hello.api.{GreetingMessageChanged, HellolagomService}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait UserService extends Service {

  val TOPIC_NAME = "userdata"

  def greetUser(username: String): ServiceCall[NotUsed, String]

  def testUser(): ServiceCall[NotUsed, UserData]

  override final def descriptor: Descriptor = {

    import Service._
    named("welcome")
      .withCalls(
        restCall(Method.GET, "/user-data/api", testUser _),
        restCall(Method.GET,"/user/api/:username", greetUser _)
      )
      .withAutoAcl(true)
      .withTopics(
        topic(TOPIC_NAME, usersTopic _)
      )
    // @formatter:on
  }

  def usersTopic(): Topic[UserData]

}

case class UserData(userId: Int, id: Int, title:String, body: String)

object UserData {

  implicit val format: Format[UserData] = Json.format[UserData]

}
