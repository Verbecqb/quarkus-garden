import React from "react";
import moment from 'moment';
import "bootstrap/dist/css/bootstrap.min.css";

import {Tooltip} from "@nextui-org/react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import SliderDate from '@/app/ui/sliderWithDates';

import {getGardenMap} from '@/app/lib/data';

import {
    GardenMap,
    MapObject, MapObjectCells
} from "@/app/lib/model_definition";


// This part is important!
export const dynamic = "force-dynamic";

export default async function GardenMapWrapper(
{
    searchDate,
}: {
    searchDate?: Date;
}) {

    const gardenMap: GardenMap[] = await getGardenMap();


    return (
        <>

            <SliderDate />

            {
                gardenMap.map((garden: GardenMap) => (
                    <Container key={garden.id}>
                        {
                            garden.mapObjects
                                // TODO add a filter to only return MapObject of Type Raised Bed
                                .sort((a1, a2) => a1.id - a2.id) // sort MapObject by lowest id
                                .map((mapObject: MapObject) => (

                                    <div key={mapObject.id}>
                                        <h3>{mapObject.name}</h3>

                                        <RaisedBedWrapper rows={mapObject.width} columns={mapObject.length}
                                            growingMapObject={mapObject} day={searchDate ?? moment().toDate()} />

                                    </div>
                                )
                            )

                        }
                    </Container>
                ))
            }
        </>
    )
}


export async function RaisedBedWrapper({rows, columns, growingMapObject, day}: {
    rows: number,
    columns: number,
    growingMapObject: MapObject,
    day: Date
}) {

    // @ts-ignore
    const rows_array: number[] = [...Array(rows).keys()];
    // @ts-ignore
    const columns_array: number[] = [...Array(columns).keys()];


    // Filter ItemAggregate to only show Cells (taken) on the passed moment (comparison at day level)
    const cellFilteredToDayOnly: MapObjectCells[] =
        growingMapObject.cells.filter((cell) =>
            moment(cell.day).isSame(day, 'day')
        );

    // Return TRUE if cell allocation exists
    function hasPlantAllocated(i: number, j: number) {
        return cellFilteredToDayOnly.filter((cell) =>
            cell.coordinateX == i && cell.coordinateY == j).length > 0;
    }

    // Return the ItemID if a cell allocation exists (taken) for coordinates (i,j). Undefined otherwise.
    function itemIdAtPosition(i: number, j: number) {
        return cellFilteredToDayOnly.findLast((cell) =>
            cell.coordinateX == i && cell.coordinateY == j)?.item_id ?? undefined;
    }

    return (
        <>
            {rows_array.map((i: number) => (

                    <Row key={i}>
                        {
                            columns_array.map((j) =>
                                (
                                    <Col key={j} className={hasPlantAllocated(i, j) ? "bg-green-700" : "bg-yellow-800"}>

                                        {
                                            hasPlantAllocated(i, j) ? (
                                                <Tooltip content={
                                                    <div className="px-1 py-2">
                                                        <div
                                                            className="text-small font-bold">{itemIdAtPosition(i, j)}</div>
                                                        <div className="text-tiny">This is a custom tooltip
                                                            content
                                                        </div>
                                                    </div>
                                                }>
                                                    <p>X</p>
                                                </Tooltip>
                                            ) : <p>0</p>
                                        }
                                    </Col>
                                )
                            )
                        }
                    </Row>
                )
            )}
        </>
    );
}