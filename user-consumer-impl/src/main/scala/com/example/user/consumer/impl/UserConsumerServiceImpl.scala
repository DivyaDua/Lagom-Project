package com.example.user.consumer.impl

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem, Cancellable, Props}
import com.example.user.api.UserService
import com.example.user.consumer.api.UserConsumerService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class UserConsumerServiceImpl(userService: UserService)(implicit ec: ExecutionContext) extends UserConsumerService {

  /*val Tick = "tick"

  val system = ActorSystem("userActorSystem")
  val userActor: ActorRef = system.actorOf(Props(classOf[UserActor], this))

  val cancellable: Cancellable = system.scheduler.schedule(0.milliseconds,
    50.milliseconds,
    userActor,
    Tick)
*/
  override def getUserData(): ServiceCall[NotUsed, String] = ServiceCall{_ =>
    println("Executing get user data method")
    Future.successful("Successfully executed")
  }

  //This cancels further Ticks to be sent
  //cancellable.cancel()
}
