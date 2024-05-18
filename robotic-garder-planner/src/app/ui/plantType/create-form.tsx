import {RadioGroup, Radio, Input, Slider} from "@nextui-org/react";
import {Accordion, AccordionItem} from "@nextui-org/react";

import {createPlantType} from '@/app/lib/actions';

import ButtonForm from '@/app/ui/button-form';

export default function Form() {

    return (

        <>

            <section
                className="max-w-4xl p-6 mx-auto bg-indigo-600 rounded-md shadow-md dark:bg-gray-800 mt-20">
                <h2 className="text-xl font-bold text-white capitalize dark:text-white">Create a plant type</h2>

                <form action={createPlantType}>

                    <div className="grid grid-cols-1 gap-6 mt-4 sm:grid-cols-2">
                        <div>
                            <label className="text-white dark:text-gray-200" htmlFor="plant_name">Plant
                                name</label>
                            <Input
                                type="input"
                                name="plant_name"
                                placeholder="Enter a name"
                                className="max-w-xs"
                            />

                        </div>

                        <div>

                            <label className="text-white dark:text-gray-200" htmlFor="plant_type">Select Plant
                                Type</label>
                            <RadioGroup
                                orientation="horizontal"
                                defaultValue="SINGLE"
                                name="plant_type"
                                id="plant_type"
                                className="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-300 rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-500 dark:focus:border-blue-500 focus:outline-none focus:ring"
                            >
                                <Radio value="SINGLE">Unique</Radio>
                                <Radio value="RECURRING">Recurring</Radio>
                            </RadioGroup>
                        </div>
                        <div className="flex flex-col gap-2 w-full h-full max-w-md items-start justify-center">
                            <label className="text-white dark:text-gray-200" htmlFor="passwordConfirmation">Growing
                                period</label>
                            <Slider
                                name="growing_months"
                                size="lg"
                                hideValue={true}
                                color="secondary"
                                showSteps={true}
                                step={1}
                                maxValue={12}
                                minValue={1}
                                defaultValue={[3, 8, 9, 12]}
                                marks={[
                                    {
                                        value: 1,
                                        label: "Jan.",
                                    },
                                    {
                                        value: 3,
                                        label: "Mar.",
                                    },
                                    {
                                        value: 6,
                                        label: "Jun",
                                    },
                                    {
                                        value: 9,
                                        label: "Sep.",
                                    },
                                    {
                                        value: 12,
                                        label: "Dec.",
                                    }
                                ]}
                                className=""
                            />
                        </div>
                        <div>
                            <label className="text-white dark:text-gray-200" htmlFor="">Space</label>
                            <Input
                                type="number"
                                label="width"
                                name="space_width"
                                size="sm"
                                isRequired />
                            <Input
                                type="number"
                                label="length"
                                name="space_length"
                                size="sm"
                                isRequired />

                        </div>
                        <div className="grid grid-cols-1 gap-1 mt-4 sm:grid-cols-1">
                            <label className="text-white dark:text-gray-200" htmlFor="grow_duration">Grow
                                duration</label>
                            <Input
                                placeholder="0"
                                name="grow_duration"
                                startContent={
                                    <div className="pointer-events-none flex items-center">
                                    </div>
                                }
                                endContent={
                                    <div className="flex items-center">
                                        <label className="sr-only" htmlFor="duration">
                                            duration
                                        </label>
                                        <select
                                            className="outline-none border-0 bg-transparent text-default-400 text-small"
                                            id="duration"
                                            name="duration"
                                        >
                                            <option value="D">Days</option>
                                            <option value="W">Weeks</option>
                                        </select>
                                    </div>
                                }
                                type="number"
                                className="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-300 rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-500 dark:focus:border-blue-500 focus:outline-none focus:ring"
                            />

                        </div>

                    </div>

                    <div className="flex justify-end mt-6 text-white">
                        <ButtonForm text="Save"/>
                    </div>
                </form>
            </section>

        </>
    );

}