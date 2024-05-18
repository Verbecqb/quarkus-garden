import GardenMapWrapper from '@/app/ui/gardenmap/GardenMap'
import CreateItemForm from "@/app/ui/plantItem/CreateItemForm";

import {Suspense} from "react";
import {Divider} from "@nextui-org/react";

export default function Page ({
    searchParams,
}: {
    searchParams?: {
        day?: string,
    },
}) {

    const searchedDate: Date = (searchParams?.day && new Date(searchParams.day)) || new Date();


    return (
        <>
            <div className="w-full">
                <div className="flex w-full items-center justify-between">
                    <h1 className="text-2xl">Garden map</h1>
                </div>
                <div>
                    <CreateItemForm />
                </div>
                    <Divider className="my-4" />
                <div>
                    <Suspense fallback="Loading...">
                        <GardenMapWrapper searchDate={searchedDate}/>
                    </Suspense>
                </div>

            </div>



        </>
    );
}