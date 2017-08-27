package com.example.user.consumer.impl

import com.example.user.api.UserService
import com.example.user.consumer.api.UserConsumerService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

class UserConsumerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new UserConsumerApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new UserConsumerApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[UserConsumerService])
}

abstract class UserConsumerApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with LagomKafkaClientComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[UserConsumerService](wire[UserConsumerServiceImpl])

  //Bind external service
  lazy val hellolagomService: UserService = serviceClient.implement[UserService]

}

