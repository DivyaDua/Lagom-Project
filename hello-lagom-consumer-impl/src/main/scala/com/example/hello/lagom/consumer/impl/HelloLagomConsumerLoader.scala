package com.example.hello.lagom.consumer.impl

import com.example.hello.api.HellolagomService
import com.example.hello.lagom.consumer.api.HelloLagomConsumerService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

class HelloLagomConsumerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new HelloConsumerApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new HelloConsumerApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[HelloLagomConsumerService])
}

abstract class HelloConsumerApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with LagomKafkaClientComponents
    with AhcWSComponents {

  // Bind the services that this server provides
  override lazy val lagomServer: LagomServer = serverFor[HelloLagomConsumerService](wire[HelloLagomConsumerServiceImpl])

  //Bind the HellolagomService client
  lazy val hellolagomService: HellolagomService = serviceClient.implement[HellolagomService]


}

