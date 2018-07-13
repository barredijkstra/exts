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

import org.scalatest.{FlatSpec, Matchers}

/**
  * Common test cases and logic for spray marshalling/unmarshalling of enumerations.
  */
private[spray] trait EnumTests { self: FlatSpec with Matchers =>

  import spray.json._
  import DefaultJsonProtocol._

  type EnumType

  case class Foo(value: EnumType)

  implicit def enumTypeFormat: JsonFormat[EnumType]

  implicit def fooFormat: RootJsonFormat[Foo] = jsonFormat1(Foo)

  def enumFooValue: EnumType

  def enumBarValue: EnumType

  "Marshalling an enum" should "use the enum name as JSON value" in {
    val json = Foo(enumBarValue).toJson.compactPrint
    json shouldEqual """{"value":"BAR"}"""
  }
  "Unmarshalling an enum" should "succeed for an exact match" in {
    val obj = """{"value":"FOO"}""".parseJson.convertTo[Foo]
    obj shouldEqual Foo(enumFooValue)
  }
  it should "throw an exception for a invalid-case value" in {
    a[DeserializationException] should be thrownBy """{"value":"bar"}""".parseJson.convertTo[Foo]
  }
  it should "throw an exception for a non-existing value" in {
    a[DeserializationException] should be thrownBy """{"value":"raboof"}""".parseJson.convertTo[Foo]
  }
  it should "throw an exception for an ordinal as value" in {
    a[DeserializationException] should be thrownBy """{"value":42}""".parseJson.convertTo[Foo]
  }
}
