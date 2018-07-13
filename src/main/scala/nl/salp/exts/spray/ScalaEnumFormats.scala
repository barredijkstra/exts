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

package nl.salp.exts.spray

import spray.json._

import scala.util.{Failure, Success, Try}

/**
  * Spray formats for Scala enums.
  */
trait ScalaEnumFormats {

  /**
    * Create a new JsonFormat for a Scala enum, mapping between the enum value and the name.
    *
    * @param enum The enum object.
    * @tparam E The type of the enum.
    * @return The format.
    */
  def jsonScalaEnumFormat[E <: scala.Enumeration](enum: E): JsonFormat[E#Value] =
    new JsonFormat[E#Value] {
      override def read(json: JsValue): E#Value = json match {
        case JsString(name) =>
          Try(enum.withName(name)) match {
            case Success(e) => e
            case Failure(e) => deserializationError(s"$name is not a valid ${enum.getClass.getName} value", e)
          }
        case _ =>
          deserializationError(s"$json is not a valid ${enum.getClass.getName} value")
      }

      override def write(obj: E#Value): JsValue =
        JsString(obj.toString)
    }
}

object ScalaEnumFormats extends ScalaEnumFormats
