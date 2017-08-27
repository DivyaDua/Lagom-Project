package com.example.user.impl

import akka.NotUsed
import com.example.hello.api
import com.example.hello.impl.{GreetingMessageChanged, HellolagomEvent}
import com.example.user.api.{ExternalUserService, UserData, UserService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, EventStreamElement, PersistentEntityRegistry}

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, externalUserService: ExternalUserService)(implicit ec: ExecutionContext) extends UserService {

  override def greetUser(username: String): ServiceCall[NotUsed, String] = ServiceCall{ _ =>
    Future.successful(s"Welcome $username!")
  }

  override def testUser() = ServiceCall { _ =>
    val result: Future[UserData] = externalUserService.getUser().invoke()
    result.map(response => response)
  }

  override def usersTopic(): Topic[UserData] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(UserEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(userEvent: EventStreamElement[UserEvent]): UserData= {
    userEvent.event match {
     case userEvent => UserData(userEvent)
    }
  }
}

sealed trait UserEvent extends AggregateEvent[UserEvent] {
  def aggregateTag = UserEvent.Tag
}

object UserEvent {
  val Tag: AggregateEventTag[UserEvent] = AggregateEventTag[UserEvent]
}
