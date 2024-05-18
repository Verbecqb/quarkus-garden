'use client';

import {Button} from "@nextui-org/react";
import {useFormStatus} from 'react-dom'

export default function ButtonForm({text,} : {text: string}) {

    const {pending} = useFormStatus();

    return (
        <>
            <Button type="submit" radius="md" size="lg" variant="flat" color="secondary" isLoading={pending}>
                {text}
            </Button>
        </>
    );
}