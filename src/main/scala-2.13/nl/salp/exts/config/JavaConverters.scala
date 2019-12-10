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

import java.time.{Duration => JDuration}

import scala.concurrent.duration.FiniteDuration

private[config] object JavaConverters {
  import scala.jdk.CollectionConverters._
  def toScalaFiniteDuration(duration: JDuration): FiniteDuration = {
    import scala.jdk.DurationConverters._
    duration.toScala
  }
  def toScalaSeq[E](list: java.util.List[E]): Seq[E] =
    list.asScala.toSeq

  def toScalaSet[E](set: java.util.Set[E]): Set[E] =
    set.asScala.toSet
}
