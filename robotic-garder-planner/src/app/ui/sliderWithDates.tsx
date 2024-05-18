'use client'

import React from "react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";

import {SliderStepMark, SliderValue} from "@nextui-org/react";
import {Slider} from "@nextui-org/react";

import moment, {Moment} from 'moment';


export default function SliderDate() {

    const { replace } = useRouter();
    const pathname = usePathname();
    const searchParams = useSearchParams();

    // Setup for SLIDER by day
    const MAX_DURATION: number = 30;
    const DISPLAYED_MARKS_STEPS: number = 5;

    // Array pre-filled with Moment - from today to today+30days
    const dates_array: Moment[] = [...Array(MAX_DURATION).fill(null).map((_, i) => moment().add(i, 'd'))];

    // Build the marks array used by the
    const marks :  SliderStepMark[] = [...Array(MAX_DURATION).fill(null).map((_, i) =>
        Object({ value: i, label: dates_array[i].format('DD-MMM')})).filter((_,i) => i % DISPLAYED_MARKS_STEPS === 0)];

    // Set the query params in URL with "?day=YYYY-MM-DD"
    // Doc - https://nextjs.org/learn/dashboard-app/adding-search-and-pagination
    function onChangeEnd(val: number | number[]) {
        const params = new URLSearchParams(searchParams);
        params.set('day', (!Array.isArray(val) && dates_array[val].format('YYYY-MM-DD')) || moment().format('YYYY-MM-DD'));
        console.debug("params - " + params);
        replace(`${pathname}?${params}`);
    }

    function byIdxByDate(date: string | undefined) {
        if (date === undefined) return 0;
        // Find the index. If not found, findIndex returns -1
        const idx = dates_array.findIndex(e => moment(e).format('YYYY-MM-DD') == date) ;
        return idx >= 0 ? idx : 0;
    }


    // @ts-ignore
    return (
        <Slider
            size="md"
            step={1}
            color="foreground"
            label="Day"
            showSteps={true}
            maxValue={MAX_DURATION-1}
            minValue={0}
            defaultValue={byIdxByDate(searchParams.get('day')?.toString())}
            marks={marks}
            className="max-w-md"
            getValue={(value: SliderValue) => (dates_array[value].format('DD-MMM'))}
            onChangeEnd={onChangeEnd}
                >
        </Slider>
    );
}

