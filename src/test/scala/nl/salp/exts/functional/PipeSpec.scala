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

package nl.salp.exts.functional

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PipeSpec extends AnyFlatSpec with Matchers {
  def readValue(value: String): Int = value.toInt
  def square(value: Int): Int = value * value

  "Piping of methods" should "chain the method calls" in {
    "8" |> readValue |> square shouldEqual 64
  }
}
