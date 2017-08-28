package com.example.user.impl

import akka.actor.{Actor, Props}
import com.example.user.api.{ExternalUserService, UserData}
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserActor(externalUserService: ExternalUserService) extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    case s: String =>
      val result: Future[UserData] = externalUserService.getUser().invoke()
      Logger.info("----------User Data--------------")
      result.map(user => Logger.info(user.toString))

    case _ => Logger.info("Did not get any user data...!!")
  }

}

object UserActor{

  def props(externalUserService: ExternalUserService): Props = Props(classOf[UserActor], externalUserService)
}