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
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable

class EnumeratumFormatsSpec extends AnyFlatSpec with Matchers with EnumTests {

  sealed trait EnumeratumEnum extends EnumEntry

  object EnumeratumEnum extends Enum[EnumeratumEnum] {

    case object FOO extends EnumeratumEnum

    case object BAR extends EnumeratumEnum

    override def values: immutable.IndexedSeq[EnumeratumEnum] = findValues
  }

  import EnumeratumFormats._

  override type EnumType = EnumeratumEnum

  override implicit val enumTypeFormat = jsonEnumeratumFormat(EnumeratumEnum)
  override val enumFooValue = EnumeratumEnum.FOO
  override val enumBarValue = EnumeratumEnum.BAR
}
