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

import enumeratum.{Enum, EnumEntry}
import spray.json._

/**
  * Spray formats for enumeratum enums.
  */
trait EnumFormats extends DefaultJsonProtocol {
  /**
    * Create a new JsonFormat for an enumeratum enum, using case-insensitve translation from JSON string to enum value name.
    *
    * @param enumCompanion The companion object.
    * @tparam T The enum.
    * @return The format.
    */
  def jsonEnumFormat[T <: EnumEntry](enumCompanion: Enum[T]): JsonFormat[T] =
    new JsonFormat[T] {
      override def read(json: JsValue): T = json match {
        case JsString(name) =>
          enumCompanion
            .withNameInsensitiveOption(name)
            .getOrElse(deserializationError(s"$name should be one of (${enumCompanion.values.map(_.entryName).mkString(", ")})"))
        case _ =>
          deserializationError(s"${json.toString()} should be a string of value (${enumCompanion.values.map(_.entryName).mkString(", ")})")
      }

      override def write(obj: T): JsValue = JsString(obj.entryName)
    }
}
