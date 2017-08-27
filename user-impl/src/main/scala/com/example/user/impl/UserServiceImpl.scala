package com.example.user.impl

import akka.NotUsed
import com.example.user.api
import com.example.user.api.{ExternalUserService, UserData, UserService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, EventStreamElement, PersistentEntityRegistry}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq
import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl(persistentEntityRegistry: PersistentEntityRegistry,externalUserService: ExternalUserService)(implicit ec: ExecutionContext) extends UserService {

  override def greetUser(username: String): ServiceCall[NotUsed, String] = ServiceCall{ _ =>
    Future.successful(s"Welcome $username!")
  }

  override def testUser() = ServiceCall { _ =>
    val result: Future[UserData] = externalUserService.getUser().invoke()
    result.map(response => response)
  }

  override def usersTopic(): Topic[api.UserDataChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(UserEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(userEvent: EventStreamElement[UserEvent]): api.UserDataChanged= {
    userEvent.event match {
     case UserDataChanged(userId, id, title, body) => api.UserDataChanged(userId, id, title, body)
    }
  }
}

sealed trait UserEvent extends AggregateEvent[UserEvent] {
  def aggregateTag = UserEvent.Tag
}

object UserEvent {
  val Tag: AggregateEventTag[UserEvent] = AggregateEventTag[UserEvent]
}

case class UserDataChanged(userId: Int, id: Int, title:String, body: String) extends UserEvent

object UserDataChanged {

  implicit val format: Format[UserDataChanged] = Json.format[UserDataChanged]

}

object UserSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[UserData],
    JsonSerializer[UserDataChanged]
  )
}