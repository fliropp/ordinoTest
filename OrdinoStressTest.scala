package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._


class OrdinoStressTest extends Simulation {

  val randToken = Iterator.continually(Map("token" -> randoken))
  val randPub = Iterator.continually(Map("publication" -> randub))

  val r = scala.util.Random
  var pubs = Array(
    "11-58d0e7efc434617438cdbb7a",
    "11-58cbc40c53215e0ad6b5518b",
    "11-58d0e60dbaa9743445d26111",
    "11-589190e061fbe107ab126444",
    "11-58cbbe5453215e0ad6b53f7d",
    "11-58cbbcdecb73e217590d7f6e",
    "11-58358f882edb6835503d94e5",
    "11-57c97affd410453c5c0eb9b9",
    "11-58cbad17cb73e217590d5be0",
    "11-58caa78853215e0ad6b4bfc8",
    "11-58caa5d0cb73e217590cf883",
    "11-57db8e48b3fa23cd74eef696",
    "11-58cbf3bd53215e0ad6b5d907",
    "11-58cba88ecb73e217590d5745",
    "11-58cbaadacb73e217590d59c2"
  )

  def randoken() : String = {
    var token:String = ""
    for (i <- 1 to 6) {
      token = token + Integer.toString(r.nextInt(10))
    }
    return token
  }

  def randub() : String = {
    var i = r.nextInt(15)
    var publication:String = pubs(i)
    return publication
  }

  val httpProtocol = http
    .baseUrl("http://localhost:9840") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/json,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers


  val scn = scenario("OrdinoTest") // A scenario is a chain of requests and pauses
        .feed(randPub)
       .feed(randToken)
       .exec(http("Get Markup").get("/api/ordino/v1/lists/${publication}/rendered?token=${token}"))
      /*.check(status.is(200), jsonPath("$.id"))
        .saveAs("fpid")*/

  setUp(scn.inject(rampUsers(5) during (1 seconds)).protocols(httpProtocol))
    .assertions(global.responseTime.max.lt(500))
}