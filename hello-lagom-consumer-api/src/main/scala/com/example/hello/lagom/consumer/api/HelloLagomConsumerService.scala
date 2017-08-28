package com.example.hello.lagom.consumer.api


import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait HelloLagomConsumerService extends Service {

  override def descriptor: Descriptor = {
    import Service._

    named("hello-consumer-service").withCalls(
      restCall(Method.POST, "/hello-consumer/api/", postValue _)
    ).withAutoAcl(true)
  }

  def postValue(): ServiceCall[NotUsed, String]

}
