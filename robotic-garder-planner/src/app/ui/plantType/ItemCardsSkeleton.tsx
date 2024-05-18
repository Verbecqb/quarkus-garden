import {Card, Skeleton} from "@nextui-org/react";

// Loading animation
const shimmer =
    'before:absolute before:inset-0 before:-translate-x-full before:animate-[shimmer_2s_infinite] before:bg-gradient-to-r before:from-transparent before:via-white/60 before:to-transparent';

export  function ItemCardSkeleton() {
    return (


    <Card className={`${shimmer} w-[200px] space-y-5 p-4 m-10 radius="lg" flex-col-reverse`}>
        <Skeleton className="rounded-lg">
            <div className="h-24 rounded-lg bg-default-300"></div>
        </Skeleton>
        <div className="space-y-3">
            <Skeleton className="w-3/5 rounded-lg">
                <div className="h-3 w-3/5 rounded-lg bg-default-200"></div>
            </Skeleton>
            <Skeleton className="w-4/5 rounded-lg">
                <div className="h-3 w-4/5 rounded-lg bg-default-200"></div>
            </Skeleton>
            <Skeleton className="w-2/5 rounded-lg">
                <div className="h-3 w-2/5 rounded-lg bg-default-300"></div>
            </Skeleton>
        </div>
    </Card>
    );
}

export function CardsSkeleton() {
    return (
        <>
            <ItemCardSkeleton />
            <ItemCardSkeleton />
            <ItemCardSkeleton />
            <ItemCardSkeleton />
        </>

    )
}
