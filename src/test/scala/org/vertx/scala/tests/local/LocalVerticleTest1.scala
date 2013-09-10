package local

import org.junit.Test
import org.vertx.java.core.buffer.Buffer
import org.vertx.java.core.AsyncResult
import org.vertx.java.core.http.HttpServer

import org.vertx.testtools.VertxAssert._
import org.vertx.testtools.TestVerticle

import org.vertx.scala.core._
import org.vertx.scala.core.eventbus.{ Message, EventBus }
import org.vertx.scala.core.http.WebSocket

import org.vertx.java.core.logging.Logger
import org.vertx.java.core.logging.impl.LoggerFactory

import org.vertx.java.core.json.{ JsonObject, JsonArray }

import org.vertx.scala.platform.Verticle


class LocalTestVerticle1 extends Verticle {
  import org.vertx.scala.core.eventbus.EventBus._
  val hdl: EventBusHandler[String] = EventBus.toBusHandler(
    (msg: Message[String]) => {
      assertNotNull(msg.body)
      testComplete()
    })

  override def start(future: Future[Void]):Unit = {
    start()
    vertx.eventBus.registerHandler("some-address")(hdl, rst => {
      if (!rst.succeeded) {
        future.setFailure(rst.cause())
        fail()
      } else {
        future.setResult(null)
        testComplete()
      }
    })
  }

} // LocalTestVerticle

class LocalVerticleTest1 extends TestVerticle {
  @Test
  def testMyTestVerticle() {
    import org.vertx.scala.core.FunctionConverters._
      container.deployVerticle(classOf[LocalTestVerticle1].getName,
        (ar: AsyncResult[String]) => {
          if (ar.succeeded) {
            assertTrue(ar.succeeded)
            testComplete()
          } else {
            ar.cause.printStackTrace()
            fail()
          }
        })
  }
} // LocalVerticleTest
