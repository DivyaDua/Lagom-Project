package com.example.hello.lagom.consumer.api


import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait HelloLagomConsumerService extends Service {

  override def descriptor: Descriptor = {
    import Service._

    named("wordCounts").withCalls(
      restCall(Method.GET, "/hello-consumer/api/", getValue _),
      restCall(Method.POST, "/hello-consumer/api/", postValue _)
    ).withAutoAcl(true)
  }

  def getValue(): ServiceCall[NotUsed, String]

  def postValue(): ServiceCall[NotUsed, String]
}
