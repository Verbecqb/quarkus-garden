import {Button} from "@nextui-org/react";

import {postItem} from "@/app/lib/actions";
import {getPlantTypes} from "@/app/lib/data";
import {PlantTypeFields} from "@/app/lib/model_definition";
import UISelectClientComponent from "@/app/ui/plantItem/UISelectClientComponent";

export default async function CreateItemForm() {

    const plantTypes: PlantTypeFields[] = await getPlantTypes();

    return (
        <>

            <form action={postItem}>

                <UISelectClientComponent plantTypes={plantTypes} />
                <Button type="submit" radius="md" size="lg" variant="flat" color="secondary">
                    Create
                </Button>

            </form>

        </>
    );
}