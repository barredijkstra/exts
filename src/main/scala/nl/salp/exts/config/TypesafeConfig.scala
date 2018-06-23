/*
 * Copyright 2018 Barre Dijkstra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.salp.exts.config

import java.util.concurrent.TimeUnit
import java.{time => jtime}

import com.typesafe.config.{Config, ConfigException}

import scala.collection.JavaConverters._
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success, Try}

/**
  * Helper for typesafe config based configurations.
  */
trait TypesafeConfig {
  /** The actual typesafe configuration instance. */
  def config: Config

  /**
    * Implicit conversions from Java Duration to a Scala FiniteDuration for with Config.getDuration()
    *
    * @param duration The Java duration.
    * @return The Java Duration as Scala FiniteDuration.
    */
  implicit protected def duration2FiniteDuration(duration: jtime.Duration): FiniteDuration =
    FiniteDuration(duration.toNanos, TimeUnit.NANOSECONDS)

  /**
    * Wraps reading a config value as an option, returning a None when it was not found.
    *
    * @param f Regular function to read a value from the config.
    * @tparam T The config value type.
    * @return The option value of the option.
    */
  protected def asOption[T](f: Config => T): Option[T] = Try(f(config)) match {
    case Success(value) => Some(value)
    case Failure(_: ConfigException.Missing) => None
    case Failure(ex) => throw ex
  }

  /**
    * Get a Map from the config file.
    *
    * Structure in a HOCON config file must be:
    * {{{
    *   myKey = [
    *     {"key": value},
    *     {"otherKey": otherValue}
    *   ]
    * }}}
    *
    * @param path The path of the map.
    * @return The map.
    */
  protected def getMap(path: String): Map[String, AnyRef] = (for {
    obj <- config.getObjectList(path).asScala
    entry <- obj.entrySet().asScala
  } yield (entry.getKey, entry.getValue.unwrapped())).toMap
}

