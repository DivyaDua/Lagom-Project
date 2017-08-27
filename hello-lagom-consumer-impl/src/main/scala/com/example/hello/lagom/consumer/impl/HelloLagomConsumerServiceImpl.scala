package com.example.hello.lagom.consumer.impl

import akka.{Done, NotUsed}
import akka.stream.scaladsl.Flow
import com.example.hello.api.{GreetingMessage, GreetingMessageChanged, HellolagomService}
import com.example.hello.lagom.consumer.api.HelloLagomConsumerService
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.{ExecutionContext, Future}

class HelloLagomConsumerServiceImpl(hellolagomService: HellolagomService)(implicit ec: ExecutionContext) extends HelloLagomConsumerService{

  hellolagomService.greetingsTopic
    .subscribe
    .atLeastOnce(
      Flow[GreetingMessageChanged].map{ msg =>
        putGreetingMessage(msg)
        Done
      }
    )

  private def putGreetingMessage(greetingMessage: GreetingMessageChanged) = {
    println("Message is being consumed")
  }


  def getValue(): ServiceCall[NotUsed, String] = ServiceCall{_ =>
    println("Inside get value method.")
    Future.successful("Done")
  }

  def postValue(): ServiceCall[NotUsed, String] = ServiceCall{_ =>
    println("Inside post value method.")
    Future.successful("Done")
  }
}