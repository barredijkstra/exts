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

package nl.salp.exts

import org.scalatest.{FlatSpec, Matchers}

class TagSpec extends FlatSpec with Matchers {

  import tag._

  sealed trait LeftValueTag

  sealed trait RightValueTag

  type LeftValue = Long @@ LeftValueTag
  type RightValue = Long @@ RightValueTag

  "Tagged values" should "behave as normal values" in {
    val left: LeftValue = tag[LeftValueTag][Long](42L)
    val right: RightValue = tag[RightValueTag][Long](8L)
    left shouldEqual 42L
    right shouldEqual 8L
    (left + right) shouldEqual 50L
  }
}
