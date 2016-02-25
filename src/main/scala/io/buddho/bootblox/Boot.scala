// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.buddho.bootblox

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import io.buddho.bootblox.config.Config
import io.buddho.bootblox.modules._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


trait RuntimeModules extends RoutesModule with AkkaModule with ConfigModule {

  def cleanup(): Unit = {
    system.terminate()
  }

}


class Boot extends StrictLogging {

  def start(cfg: Config): (Future[ServerBinding], RuntimeModules) = {

    val modules = new RuntimeModules {

      override val config: Config = cfg

      override implicit lazy val system: ActorSystem = ActorSystem("main")
      override implicit def executionContext: ExecutionContext = system.dispatcher
      override implicit lazy val materializer: Materializer = ActorMaterializer()

    }

    implicit val ec = modules.system
    implicit val mat = modules.materializer

    (Http().bindAndHandle(
      modules.routes,
      modules.config.server.host,
      modules.config.server.port),

      modules)
  }

}


object Boot extends App with StrictLogging {

  val config = Config(ConfigFactory.load(), args) match {
    case None => Config.help(); sys.exit(1)
    case Some(c) => c
  }

  val (binding: Future[ServerBinding], modules: RuntimeModules) = new Boot().start(config)

  implicit val ec = modules.executionContext

  binding.onComplete {
    case Success(b) =>
      logger.info(s"Server started on ${b.localAddress}")
      sys.addShutdownHook {
        b.unbind()
        modules.cleanup()
        logger.info("Server stopped")
      }
    case Failure(e) =>
      logger.error(s"Cannot start server.", e)
      sys.addShutdownHook {
        modules.cleanup()
        logger.info("Server stopped")
      }
  }

}
