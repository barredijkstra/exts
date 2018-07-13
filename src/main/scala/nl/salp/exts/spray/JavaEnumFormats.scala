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

import scala.reflect.ClassTag

/**
  * Spray formats for Java enums.
  */
trait JavaEnumFormats {

  /**
    * Create a new JsonFormat for a Java enum, mapping between the enum value and the name.
    *
    * @tparam E The type of the enum.
    * @return The format.
    */
  def jsonJavaEnumFormat[E <: java.lang.Enum[E] : ClassTag]: JsonFormat[E] =
    new JsonFormat[E] {

      import scala.reflect._

      val m = classTag[E]

      override def read(json: JsValue): E = json match {
        case JsString(name) =>
          val enum = m.runtimeClass.asInstanceOf[Class[E]]
          enum.getEnumConstants
            .find(_.name equals name)
            .getOrElse(deserializationError(s"$name is not a valid ${enum.getName} value"))
        case _ =>
          deserializationError(s"$json is not a valid ${m.runtimeClass.getName} value")
      }

      override def write(obj: E): JsValue =
        JsString(obj.name)
    }
}

object JavaEnumFormats extends JavaEnumFormats