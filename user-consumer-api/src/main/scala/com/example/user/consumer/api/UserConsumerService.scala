package com.example.user.consumer.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.lightbend.lagom.scaladsl.api.transport.Method

trait UserConsumerService extends Service {

  override def descriptor: Descriptor = {
    import Service._

    named("user-consumer-service").withCalls(
      restCall(Method.POST, "/hello-consumer/api/", getUserData _)
    )
    .withAutoAcl(true)
  }

  def getUserData(): ServiceCall[NotUsed, String]
}
