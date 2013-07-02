/*
 * Copyright 2013 the original author or authors.
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
package org.vertx.scala.core.file

import org.vertx.java.core.AsyncResult
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.file.{ AsyncFile => JAsyncFile }
import org.vertx.scala.core.FunctionConverters._
import org.vertx.scala.core.streams.{ WriteStream, ReadStream }
import org.vertx.scala.core.Delegator
import org.vertx.java.core.Handler

/**
 * @author Edgar Chan
 * @author swilliams
 */
object AsyncFile {
  def apply(internal: JAsyncFile) = new AsyncFile(internal)
  implicit def toScala(internal: JAsyncFile) = new AsyncFile(internal)
}

class AsyncFile(internal: JAsyncFile) extends Delegator[JAsyncFile] with ReadStream[JAsyncFile] with WriteStream[JAsyncFile] {

  def close(): Unit = internal.close()

  def close(handler: AsyncResult[Unit] => Unit): Unit = {
    internal.close(voidAsyncHandler(handler))
  }

  def write(data: Buffer, position: Int, handler: AsyncResult[Unit] => Unit): AsyncFile.this.type = {
    internal.write(data, position, voidAsyncHandler(handler))
    this
  }

  def read(buffer: Buffer, offset: Int, position: Int, length: Int, handler: AsyncResult[Buffer] => Unit): AsyncFile.this.type = {
    internal.read(buffer, offset, position, length, new Handler[AsyncResult[Buffer]] {
      def handle(event: AsyncResult[Buffer]) {
        handler(event)
      }
    })
    this
  }

  def flush(): AsyncFile.this.type = {
    internal.flush()
    this
  }

  def flush(handler: AsyncResult[Unit] => Unit): AsyncFile.this.type = {
    internal.flush(voidAsyncHandler(handler))
    this
  }

}
