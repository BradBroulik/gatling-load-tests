/*
 * Copyright 2011-2019 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package twitter

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class TwitterWebAuthenticatedLoadTest extends Simulation {

  private val httpProtocol = http
    .baseUrl("https://twitter.com") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
  private val users = csv("users.csv") 
  private val headers_POST = Map("Content-Type" -> "application/x-www-form-urlencoded") // Note the headers specific to a given request

  private val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .feed(users)
    .exec(http("public-page")
      .get("/"))
    .pause(5) // Note that Gatling has recorded real time pauses
    .exec(http("login") // Here's an example of a POST request
      .post("/sessions")
      .headers(headers_POST)
      .formParam("session[username_or_email]", "${username}}")
      .formParam("session[password]", "${password}"))
    .pause(15)
    .exec(http("notifications")
      .get("/i/notifications"))

  	setUp(scn.inject(rampUsers(1) during (30 seconds))).protocols(httpProtocol)
}
