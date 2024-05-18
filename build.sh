cd rest-item-api
mvn clean compile package

cd ../rest-items
./mvnw install -Dquarkus.container-image.build=true -DskipTests=true

cd ../rest-planner
./mvnw install -Dquarkus.container-image.build=true -DskipTests=true

cd ../rest-planting-allocation
./mvnw install -Dquarkus.container-image.build=true -DskipTests=true

cd ../
docker compose up