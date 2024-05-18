Alt-Functional coverage
------ 
:
MVP (current)
Web-application to design a garden and manually manage the plants scheduling. 
1. CRUD operation for plant configuration (e.g. product catalog)
2. Design your garden: manage growing areas (raised beds, seed tray)
3. Possibility to manually create plants, automatically assigned to an available growing location (inventory)
4. Application provides displayed daily tasks: 
Day DD-MM: plant seed of "tomatoe" on location (A,B), transplant seed from (X,Y) to (X', Y') etc..

MMP (todo)
- automatic scheduling of plantations (simple algo)
- user management 
- revamp web UI 

Future 
1. Communication with ROS robot to transform "tasks" to Robot actions
Transform https://github.com/RobotWebTools/rosbridge_suite?tab=readme-ov-file
2. Scheduling algorith using Reinforcement Learning


Alt-Microservices 
------ 

- rest-config-service
Description: manages the configuration of available Plant Types (name, growing season etc..)
TODO: manage images/blob storage
DB: yes. 

- Rest-items 
Description: manages Plant items entities for entire life cycle of the plant (seed, allocated, etc...) 
God entity design smell? 

- Robotic-garden-planner 

Description: Domain Driven Design. Garden Aggregate, composed on MapObjects which can are either "growing areas" or objects on the map. 
Function: handle the map garden CRUD operation & plant-allocation to "valid" location. 
Invariants: 
1. MapObjects cannot overlay other objects
2. "growing areas" - two plants cannot grow at the same location at given time. TODO: create a specific domain for plant-allocation 

DB: yes. Postgresql. 

robotic-garden-planner: the "front-end" powered by NextJS/React.

Alt-Model
-----


Alt-Distributed pattern 
------ 



SAGA choregrahy 

| Service         | Transaction   | Compensating trx | Inter-service comm.
| --------------- |:-------------:| ----------------:|----------------:|
| Item-service    | createItem()  | CancelItem       | Kafka Event out |
| Config-service  | getItem()     | None             | HTTP Rest/JSON  |
| Garden-planner  | are neat      | None             | Kafka Event in  |



## Resiliency

### References:
- https://quarkus.io/guides/smallrye-fault-tolerance

### Timeouts
The [`ItemService`](src/main/java/io/quarkus/sample/superheroes/fight/service/FightService.java) class uses [timeouts](https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-timeouts) from [SmallRye Fault Tolerance](https://quarkus.io/guides/smallrye-fault-tolerance) to protect against calls to the downstream [Hero](../rest-heroes), [Villain](../rest-villains), [Narration](../rest-narration), and [Location](../grpc-locations) services. Tests for these conditions can be found in [`FightServiceTests`](src/test/java/io/quarkus/sample/superheroes/fight/service/FightServiceTests.java).

### Fallbacks
The [`FightService`](src/main/java/io/quarkus/sample/superheroes/fight/service/FightService.java) class uses [fallbacks](https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-fallbacks) from [SmallRye Fault Tolerance](https://quarkus.io/guides/smallrye-fault-tolerance) to protect against calls to the downstream [Hero](../rest-heroes), [Villain](../rest-villains), [Narration](../rest-narration), and [Location](../grpc-locations) services. Tests for these conditions can be found in [`FightServiceTests`](src/test/java/io/quarkus/sample/superheroes/fight/service/FightServiceTests.java).

### Retries
Retry logic to the downstream [Hero](../rest-heroes), [Villain](../rest-villains), [Narration](../rest-narration), and [Location](../grpc-locations) services is implemented in the clients for each service.

#### Hero Client
The [`HeroRestClient`](src/main/java/io/quarkus/sample/superheroes/fight/client/HeroRestClient.java) is implemented using the [reactive rest client](https://quarkus.io/guides/rest-client-reactive). All of its configuration can be found in [`application.properties`](src/main/resources/application.properties) under the `quarkus.rest-client.hero-client` key. This client is not exposed outside of the `io.quarkus.sample.superheroes.fight.client` package.

Instead, the [`HeroClient`](src/main/java/io/quarkus/sample/superheroes/fight/client/HeroClient.java) class wraps the `HeroRestClient` and adds some resiliency to it:
- The downstream [Hero service](../rest-heroes) returns a `404` if no random [`Hero`](src/main/java/io/quarkus/sample/superheroes/fight/client/Hero.java) is found. `HeroClient` handles this case and simulates the service returning nothing.
- In the event the downstream [Hero service](../rest-heroes) returns an error, `HeroClient` adds 3 retries with a 200ms delay between each retry.


Command and Query Responsibility Segregation

Command (left side )
Query (right side) - TODO


Event Sourcing 
TODO on garden-planner service. Even sourcing 

Alt-Deployment
------ 