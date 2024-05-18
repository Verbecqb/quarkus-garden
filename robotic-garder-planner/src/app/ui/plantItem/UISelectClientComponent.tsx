"use client";

import {PlantTypeFields} from "@/app/lib/model_definition";
import {Select, SelectItem} from "@nextui-org/react";

export default function UISelectClientComponent({plantTypes,} : {plantTypes: PlantTypeFields[]}) {

    return (
        <Select
            items={plantTypes}
            name={"plantId"}
            placeholder="Select a plant type"
            className="max-w-xs"
        >
            {(plantTypes) => <SelectItem key={plantTypes.id} value={plantTypes.id}>{plantTypes.name}</SelectItem>}
        </Select>
    );
}