package apple

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ItunesApiUnauthenticatedLoadTest extends Simulation {
    private val loadTestName = "api-itunes-load-test"
    private val repeatTimes = 1 // How often to repeat the test scenario per user
    private val httpProtocol = http.baseUrl("https://itunes.apple.com")
    private val loadTestAPI = repeat(repeatTimes, "n") {
        exec(http(loadTestName)
            .get("/search?term=michael+jackson&limit=25")            
        )
    }

    private val scn = scenario("page").exec(loadTestAPI)

    // Run the load test with your preferred load (rampUsers & repeatTimes) within your preferred duration (seconds)
    setUp(scn.inject(rampUsers(10) during (20 seconds))).protocols(httpProtocol)
}