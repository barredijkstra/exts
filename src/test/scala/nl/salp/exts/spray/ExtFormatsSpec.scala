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

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ExtFormatsSpec extends AnyFlatSpec with Matchers {

  import spray.json._
  import DefaultJsonProtocol._
  import ExtFormats._

  trait FlatFormatFixture {

    case class Bar(value: String)

    case class Foo(id: Int, bar: Bar)

    implicit val barFormat: JsonFormat[Bar] = jsonFlatFormat(Bar.apply)
    implicit val fooFormat: JsonFormat[Foo] = jsonFormat2(Foo.apply)
  }

  "jsonFlatFormat" should "flatten a property when marshalled" in new FlatFormatFixture {
    val idValue: Int = 42
    val barValue: String = "foobar"

    val foo: JsObject = Foo(id = idValue, bar = Bar(barValue)).toJson.asJsObject
    foo.fields.size shouldEqual 2
    foo.getFields("id", "bar") shouldEqual scala.collection.immutable.Seq[JsValue](JsNumber(idValue), JsString(barValue))
  }
  it should "unpack a value when unmarshalling" in new FlatFormatFixture {
    """{"id":42,"bar":"foobar"}""".parseJson.convertTo[Foo] shouldEqual Foo(42, Bar("foobar"))
  }
  it should "fail unmarshalling a wrong value type" in new FlatFormatFixture {
    a[DeserializationException] should be thrownBy """{"id":42,"bar":42}""".parseJson.convertTo[Foo]
  }

  trait UuidFixture {

    case class Foo(value: java.util.UUID)

    implicit val fooFormat: JsonFormat[Foo] = jsonFormat1(Foo)

    val validJson: String = """{"value":"00000000-0000-002a-0000-00000000002a"}"""
    val invalidJson: String = """{"value":"00000000-000b-002a-0000-00000000002a"}"""
    val obj: Foo = Foo(new UUID(42L, 42L))
  }

  "A java.util.UUID" should "be marshalled as a JSON String" in new UuidFixture {
    obj.toJson.compactPrint shouldEqual validJson
    Foo(new UUID(43L, 42L)).toJson.compactPrint shouldNot equal(validJson)
  }
  it should "be unmarshalled from a JSON String" in new UuidFixture {
    validJson.parseJson.convertTo[Foo] shouldEqual obj
    validJson.parseJson.convertTo[Foo] shouldNot equal(Foo(new UUID(43L, 42L)))
  }
  it should "fail when unmarshalling an invalid JSON String" in new UuidFixture {
    a[DeserializationException] should be thrownBy """{"value":"foobar"}""".parseJson.convertTo[Foo]
  }
  it should "fail when unmarshalling an invalid JSON type" in new UuidFixture {
    a[DeserializationException] should be thrownBy """{"value":42}""".parseJson.convertTo[Foo]
  }

  trait UrlFixture {

    case class Foo(value: URL)

    implicit val fooFormat: RootJsonFormat[Foo] = jsonFormat1(Foo)

    val obj: Foo = Foo(new URL("http://www.test.com/"))
    val validJson: String = """{"value":"http://www.test.com/"}"""
    val invalidJson: String = """{"value":"https://www.other.net/"}"""
  }

  "A java.net.URL" should "be marshalled as a JSON String" in new UrlFixture {
    obj.toJson.compactPrint shouldEqual validJson
    Foo(new URL("https://www.other.net/")).toJson.compactPrint shouldNot equal(validJson)
  }
  it should "be unmarshalled from a JSON String" in new UrlFixture {
    validJson.parseJson.convertTo[Foo] shouldEqual obj
    validJson.parseJson.convertTo[Foo] shouldNot equal(Foo(new URL("https://www.other.net/")))
  }
  it should "fail when unmarshalling an invalid JSON String" in new UrlFixture {
    a[DeserializationException] should be thrownBy """{"value":"foobar"}""".parseJson.convertTo[Foo]
  }
  it should "fail when unmarshalling an invalid JSON type" in new UrlFixture {
    a[DeserializationException] should be thrownBy """{"value":42}""".parseJson.convertTo[Foo]
  }

  trait JsonValueFixture {
    val json: String =
      """{
        |  "stringValue": "foo",
        |  "numValue": 42,
        |  "decimalValue": 4.2,
        |  "boolValue": true,
        |  "objValue": {
        |    "value": "bar"
        |  }
        |}
        |""".stripMargin

    val jsObject: JsObject = json.parseJson.asJsObject
  }

  "An extended JsObject" should "provide direct reading of values" in new JsonValueFixture {
    jsObject.getString("stringValue") shouldEqual "foo"
    jsObject.getBoolean("boolValue") shouldEqual true
    jsObject.getInt("numValue") shouldEqual 42
    jsObject.getLong("numValue") shouldEqual 42L
    jsObject.getDouble("decimalValue") shouldEqual 4.2
    jsObject.getObject("objValue").getString("value") shouldEqual "bar"
  }
  it should "provide direct reading of optional values" in new JsonValueFixture {
    jsObject.optString("stringValue") shouldEqual Some("foo")
    jsObject.optString("fakeValue") shouldEqual None
    jsObject.optBoolean("boolValue") shouldEqual Some(true)
    jsObject.optBoolean("fakeValue") shouldEqual None
    jsObject.optInt("numValue") shouldEqual Some(42)
    jsObject.optInt("fakeValue") shouldEqual None
    jsObject.optLong("numValue") shouldEqual Some(42L)
    jsObject.optLong("fakeValue") shouldEqual None
    jsObject.optDouble("decimalValue") shouldEqual Some(4.2)
    jsObject.optDouble("fakeValue") shouldEqual None
    jsObject.optObject("objValue").map(_.getString("value")) shouldEqual Some("bar")
    jsObject.optObject("fakeValue").map(_.getString("value")) shouldEqual None
  }
  it should "propagate exceptions when reading optional values" in new JsonValueFixture {
    a[DeserializationException] should be thrownBy jsObject.optBoolean("decimalValue")
  }
}
