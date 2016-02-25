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

package io.buddho.bootblox.config


import com.typesafe.config.{Config => TypesafeConfig}
import scopt.OptionParser


case class Config(server: ServerConfig)

case class ServerConfig(host: Host, port: Port)


object Config {

  import com.github.kxbmap.configs.syntax._

  lazy val parser = new OptionParser[Config]("bootblox-web") {
    head("bootblox-web", "0.1.x")
  }

  def apply(config: TypesafeConfig): Config = {
    Config(server = config.get[ServerConfig]("service.server"))
  }

  def apply(config: TypesafeConfig, args: Seq[String]): Option[Config] = {
    val base = Config(config)
    parser.parse(args, base)
  }

  def help(): Unit = parser.showUsageAsError

}
