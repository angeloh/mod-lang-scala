/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.scala.core.streams

import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.streams.{ ReadStream => JReadStream }
import org.vertx.java.core.streams.{ WriteStream => JWriteStream }

/**
 * @author swilliams
 *
 */
object Pump {
  def newPump(readStream: ReadStream[JReadStream[Any]], writeStream: WriteStream[JWriteStream[Any]]) = {
    new Pump(readStream, writeStream)
  }
  def newPump(readStream: ReadStream[JReadStream[Any]], writeStream: WriteStream[JWriteStream[Any]], writeQueueMaxSize:Int) = {
    def pump = new Pump(readStream, writeStream)
    pump.writeQueueMaxSize = writeQueueMaxSize
    pump
  }
}

class Pump(readStream: ReadStream[JReadStream[Any]], writeStream: WriteStream[JWriteStream[Any]]) {

  private var writeQueueMaxSize:Int = Int.MaxValue

  private var bytesPumped:Int = 0

  private val drainHandler:() => Unit = { () =>
    readStream.resume()
  }

  private val dataHandler:Buffer => Unit = { data:Buffer =>
    writeStream.write(data)
    bytesPumped += data.length()
    if (writeStream.writeQueueFull()) {
      readStream.pause()
      writeStream.drainHandler(drainHandler)
    }
  }

  def start():Pump.this.type = {
    readStream.dataHandler(dataHandler)
    this
  }

  def stop():Pump.this.type = {
    writeStream.drainHandler(null)
    readStream.dataHandler(null)
    this
  }

}