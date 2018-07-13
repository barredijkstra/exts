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

class ScalaEnumFormatsSpec extends FlatSpec with Matchers with EnumTests {

  object ScalaEnum extends Enumeration {
    type ScalaEnum = Value
    val FOO, BAR = Value
  }

  import ScalaEnumFormats._

  override type EnumType = ScalaEnum.ScalaEnum

  override implicit val enumTypeFormat = jsonScalaEnumFormat(ScalaEnum)
  override val enumFooValue = ScalaEnum.FOO
  override val enumBarValue = ScalaEnum.BAR
}
