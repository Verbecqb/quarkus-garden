export type PlantTypeFields = {
    id: number;
    name: string;
    type: 'SINGLE' | 'RECURRING';
    space: Array<number>;
    plantingSeasons: Array<string>;
    growingDuration: number;
};

export type PlantItemFields = {
    item_id?: number;
    plant_id: number;
};

//  "cells": [
//     {
//         "id": 1,
//         "item_id": 11,
//         "day": "2024-04-14",
//         "coordinateX": 0,
//         "coordinateY": 0
//     }
export type MapObjectCells = {
    id: number;
    item_id: number;
    coordinateX : number;
    coordinateY: number;
    day: Date;
};

//             {
//                 "type": "raised_bed",
//                 "id": 1,
//                 "width": 15,
//                 "length": 10,
//                 "coordinates": [
//                     0,
//                     0
//                 ],
//                 "version": 0,
//                 "name": "bed#1",
//                 "cells": []
//             },
export type MapObject = {
    id: number;
    name: string;
    type: 'raised_bed' | 'seeding_tray';
    coordinates: Array<number>;
    width: number;
    length: number;
    cells: Array<MapObjectCells>
};



export type GardenMap = {
    id: number;
    coordinates: Array<number>;
    width: number;
    length: number;
    mapObjects: Array<MapObject>;
};
