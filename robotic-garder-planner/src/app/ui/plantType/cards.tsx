import React from 'react';
import {getPlantTypes} from '@/app/lib/data';
import moment from 'moment';

import {Card, CardBody, CardFooter,  Slider} from "@nextui-org/react";
import {Avatar} from "@nextui-org/react";

import {PlantTypeFields} from "@/app/lib/model_definition";


export default async function CardsWrapper() {

    const listPlantTypes = await getPlantTypes();

    return (
        <>
            <div className="gap-5 grid grid-cols-2 sm:grid-cols-2 m-10">
                {listPlantTypes.map((item: PlantTypeFields, index: any) => (
                    <PlantTypeCard plantType={item} key={index}/>
                ))}
            </div>
        </>
    );
}

export function PlantTypeCard(
    {plantType}: { plantType: PlantTypeFields }
) {

    return (

        <>
            <Card
                isBlurred
                className="border-none bg-background/60 dark:bg-default-100/50"
                shadow="sm"
            >
                <CardBody>
                    <div className="grid grid-cols-6 md:grid-cols-12 gap-6 md:gap-4 items-center justify-center">

                        <div className="flex flex-col col-span-6 md:col-span-12">
                            <div className="flex justify-between items-start">
                                <div className="flex flex-col gap-0">
                                    <Avatar isBordered color="default"
                                            src="https://avatars.githubusercontent.com/u/86160567?s=200&v=4"/>
                                    <h1 className="font-semibold text-foreground/90">{plantType.name}</h1>

                                </div>
                                <div><p className="text-small text-foreground/80">{plantType.type}</p></div>

                            </div>

                            <div className="flex flex-col mt-3 gap-1">

                                <div className="flex gap-1">
                                    <p className="font-semibold text-default-400 text-small">Planting season:</p>
                                    <p className="text-default-400 text-small">{plantType.plantingSeasons}</p>
                                </div>

                                <Slider
                                    aria-label="Music progress"
                                    classNames={{
                                        track: "bg-default-500/30",
                                        thumb: "w-2 h-2 after:w-2 after:h-2 after:bg-foreground",
                                    }}
                                    color="foreground"
                                    defaultValue={33}
                                    size="sm"
                                />
                                <div className="flex justify-between">
                                    <p className="text-small">JAN</p>
                                    <p className="text-small text-foreground/50">DEC</p>
                                </div>
                            </div>

                            <div className="flex gap-1">
                                <p className="font-semibold text-default-400 text-small">Space:</p>
                                <p className=" text-default-400 text-small">{plantType.space[0]}x{plantType.space[1]}</p>
                            </div>


                            <div className="flex gap-1">
                                <p className="font-semibold text-default-400 text-small">Growing duration:</p>
                                <p className="text-default-400 text-small">
                                    {
                                        moment.duration(plantType.growingDuration, 'seconds').days()
                                    } days.
                                </p>
                            </div>

                        </div>
                    </div>
                </CardBody>

                <CardFooter className="text-small justify-between">
                    <b>to be filled</b>
                </CardFooter>

            </Card>
        </>
    )
}
