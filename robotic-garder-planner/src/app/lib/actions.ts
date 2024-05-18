'use server'

import moment from 'moment';
import {z} from 'zod';
import {revalidatePath} from 'next/cache';
import {redirect} from 'next/navigation';
import {PlantItemFields} from "@/app/lib/model_definition";

const PlantTypeFormSchema = z.object({
    name: z.string(),
    type: z.enum(['SINGLE', 'RECURRING'], {
        invalid_type_error: 'Invalid plant type selected'
    }),
    space: z.array(z.coerce.number()).length(2, {message: "Size should be 2"}),
    plantingSeasons: z.array(z.string(), {
        invalid_type_error: 'Invalid plant type selected'
    }),
    growingDuration: z.coerce.number()
});


export async function createPlantType(formData: FormData) {

    // DEBUG
    console.debug(formData);

    // Prepare the data

    // TODO formData only contains one value. Growing months should be an array
    const monthAsString = moment(formData.get("growing_months"), 'M').format('MMMM').toUpperCase();

    //Validate inputs with Zod
    const validatedFields = PlantTypeFormSchema.safeParse({
        name: formData.get("plant_name"),
        type: formData.get("plant_type"),
        growingDuration: formData.get("duration") === "D"
            ? moment.duration(formData.get("grow_duration"), 'days').asSeconds()
            : moment.duration(formData.get("grow_duration"), 'weeks').asSeconds(),
        plantingSeasons: [monthAsString],
        space: [formData.get("space_width"), formData.get("space_length")]
    });

    // DEBUG output
    console.debug(validatedFields);

    // // If form validation fails, return errors early. Otherwise, continue.
    // if (!validatedFields.success) {
    //     console.error("Error during validation:" + validatedFields.error.flatten().fieldErrors);
    //     return {
    //         errors: validatedFields.error.flatten().fieldErrors,
    //         message: 'Validation issue. Failed to Create Plant Type.',
    //     };
    // }

    try {
        // 👇 Send a fetch request
        const response = await fetch("http://localhost:7807/v0/plant_types", {
            method: "POST",
            body: JSON.stringify(validatedFields.data),
            headers: {
                "content-type": "application/json",
            }
        });

    } catch (error) {
        console.log("Error - could not POST " + validatedFields.data + "\n. error: " + error);
    } finally {
        revalidatePath('/config');
        redirect('/config');
    }

}


const ItemFormSchema = z.object({
    plant_id: z.coerce.number()
});

export async function postItem(formData: FormData) {

    const validatedField = ItemFormSchema.safeParse({
        plant_id: formData.get("plantId")
    })

    console.log(JSON.stringify(validatedField.data))

    const req = await fetch("http://localhost:7806/v0/items", {
        method: "POST",
        body: JSON.stringify(validatedField.data),
        headers: {
            "content-type": "application/json",
        }
    })

    console.log("POST postItem() response status " + req.status)
    console.log("Res " + await req.clone().text())

    if (!req.status) {
        console.log("Could not POST item - " + await req.clone().text())
    }

    // Revalidate the cache
    revalidatePath('/map');
    redirect('/map');
}