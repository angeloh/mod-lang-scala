/*
 * Copyright 2011-2013 the original author or authors.
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

package org.vertx.scala.core

import java.util.{Set => JSet}
import java.util.concurrent.{ConcurrentMap => JConcurrentMap}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.concurrent.Map
import scala.collection.mutable.Set
import org.vertx.java.core.shareddata.{SharedData => JSharedData}
import java.util.{Set => JSet}
import java.util.concurrent.{ConcurrentMap => JConcurrentMap}
import org.vertx.java.core.shareddata.{SharedData => JSharedData}

/**
 * @author swilliams
 * 
 */
object SharedData {
  def apply(actual: JSharedData) =
    new SharedData(actual)
}

class SharedData(internal: JSharedData) {

  def map[K,V](name: String):Map[K,V] = {
    val jmap:JConcurrentMap[K,V] = internal.getMap(name)
    jmap
  }

  def set[T](name: String):Set[T] = {
    val jset: JSet[T] = internal.getSet(name)
    jset
  }

  def removeMap[T](name: Any):Boolean = {
    internal.removeMap(name)
  }

  def removeSet[T](name: String):Boolean = {
    internal.removeSet(name)
  }

}