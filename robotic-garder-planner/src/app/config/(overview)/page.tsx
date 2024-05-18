import Form from '@/app/ui/plantType/create-form';
import CardsWrapper from "@/app/ui/plantType/cards";
import {CardsSkeleton} from "@/app/ui/plantType/ItemCardsSkeleton";

import { Suspense } from "react";

export default function Page() {
    return (
        <>

            <Form />

            <Suspense fallback={<CardsSkeleton />}>
                <CardsWrapper />
            </Suspense>

        </>
    );
}