

export async function getPlantTypes() {
    console.debug('artifially waiting')
    await new Promise((resolve) => setTimeout(resolve, 5000));
    return fetch("http://localhost:7807/v0/plant_types", { cache: 'no-store' })
                    .then((res) => res.clone().json());

}

export async function getGardenMap() {
    const res = await fetch("http://localhost:7808/v0/map", { cache: 'no-store' });
    if (!res.ok) {
        console.log("Could not fetch GardenMap");
    }
    //console.log("Response API GET /map " + await res.clone().text());
    return res.clone().json();
}

