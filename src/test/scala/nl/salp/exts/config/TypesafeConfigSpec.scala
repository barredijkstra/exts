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

import com.typesafe.config.{Config, ConfigException, ConfigFactory}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration.{FiniteDuration, _}

class TypesafeConfigSpec extends FlatSpec with Matchers {

  object TestConfig extends TypesafeConfig {
    val durationKey = "test.duration"
    val stringMapKey = "test.stringMap"
    val intMapKey = "test.intMap"
    val valueKey = "test.value"
    override val config: Config = ConfigFactory.parseString(
      """test {
        |  duration: 10 seconds
        |  stringMap: [
        |    { foo: "bar" },
        |    { bar: "foo" }
        |  ]
        |  intMap: [
        |    { foo: 42 },
        |    { bar: 19 }
        |  ]
        |  value: "foobar"
        |}
        |""".stripMargin)

    def duration: FiniteDuration =
      config.getDuration(durationKey)

    def stringMap: Map[String, AnyRef] = getMap(stringMapKey)

    def intMap: Map[String, AnyRef] = getMap(intMapKey)

    def optValue: Option[String] = opt(_.getString(valueKey))

    def optNonExisting: Option[String] = opt(_.getString("non.existing"))

    def optInvalidType: Option[Int] = opt(_.getInt(valueKey))
  }

  "A duration" should "be read from the config" in {
    TestConfig.duration shouldEqual 10.seconds
  }

  "An optional value" should "return some when found" in {
    TestConfig.optValue shouldEqual Some("foobar")
  }
  it should "return none when not found" in {
    TestConfig.optNonExisting shouldEqual None
  }
  it should "propagate exceptions" in {
    a[ConfigException.WrongType] should be thrownBy TestConfig.optInvalidType
  }

  "A map" should "be able to contain string keys" in {
    TestConfig.stringMap("foo") shouldEqual "bar"
    TestConfig.stringMap("bar") shouldEqual "foo"
  }
  it should "be able to contain int keys" in {
    TestConfig.intMap("foo") shouldEqual 42
    TestConfig.intMap("bar") shouldEqual 19
  }
}
