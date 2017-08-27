package com.example.user.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait ExternalUserService extends Service {

  def getUser(): ServiceCall[NotUsed, UserData]

  override final def descriptor = {

    import Service._
    named("external-user-service")
      .withCalls(
        pathCall("/posts/1", getUser _)
      ).withAutoAcl(true)
    // @formatter:on
  }

}
