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

import java.net.URL
import java.util.UUID

import spray.json._

import scala.util.{Failure, Success, Try}

/**
  * Extended set of default spray formats.
  */
trait ExtFormats extends DefaultJsonProtocol {
  /**
    * Flat format, used for unwrapping JSON values and directly creating instance values.
    *
    * @param construct The construction function.
    * @param jw        The writer for the underlying JSON data type.
    * @param jr        The reader for the underlying JSON data type.
    * @tparam P The underlying JSON data type.
    * @tparam T The Scala type.
    * @return The JsonFormat for the scala type.
    */
  def jsonFlatFormat[P, T <: Product](construct: P => T)(implicit jw: JsonWriter[P], jr: JsonReader[P]): JsonFormat[T] =
    new JsonFormat[T] {
      override def read(json: JsValue): T = construct(jr.read(json))

      override def write(obj: T): JsValue = jw.write(obj.productElement(0).asInstanceOf[P])
    }

  /**
    * JsonFormat for java.net.URL
    */
  implicit val urlJsonFormat: JsonFormat[URL] = new JsonFormat[URL] {
    override def read(json: JsValue): URL = json match {
      case JsString(url) => Try(new URL(url)).getOrElse(deserializationError("Invalid URL format"))
      case _ => deserializationError("URL should be string")
    }

    override def write(obj: URL): JsValue = JsString(obj.toString)
  }

  /**
    * JsonFormat for java.util.UUID
    */
  implicit val uuidFormat: JsonFormat[UUID] = new JsonFormat[UUID] {
    override def write(obj: UUID): JsValue = JsString(obj.toString)

    override def read(json: JsValue): UUID = json match {
      case JsString(uuid) => Try(UUID.fromString(uuid)) match {
        case Success(value) => value
        case Failure(exc) => deserializationError(s"Invalid UUID: $json", exc)
      }
      case _ => deserializationError(s"Expected UUID format, got $json")
    }
  }


  /**
    * JsObject extension with various helper methods.
    *
    * @param obj The JsObject to wrap.
    */
  implicit class ExtJsObject(obj: JsObject) {
    def opt[T: JsonReader](fieldName: String): Option[T] =
      obj.fields.get(fieldName).map(_.convertTo[T])

    def get[T: JsonReader](fieldName: String): T =
      opt[T](fieldName).getOrElse(deserializationError(msg = s"Missing mandatory field $fieldName", fieldNames = fieldName :: Nil))

    val getString: String => String = get[String]
    val optString: String => Option[String] = opt[String]
    val getInt: String => Int = get[Int]
    val optInt: String => Option[Int] = opt[Int]
    val getLong: String => Long = get[Long]
    val optLong: String => Option[Long] = opt[Long]
    val getDouble: String => Double = get[Double]
    val optDouble: String => Option[Double] = opt[Double]
    val getBoolean: String => Boolean = get[Boolean]
    val optBoolean: String => Option[Boolean] = opt[Boolean]
    val optObject: String => Option[JsObject] =
      fieldName => obj.fields.get(fieldName).map(_.asJsObject)
    val getObject: String => JsObject =
      fieldName => optObject(fieldName).getOrElse(deserializationError(msg = s"Missing mandatory field $fieldName", fieldNames = fieldName :: Nil))
  }

}

object ExtFormats extends ExtFormats
