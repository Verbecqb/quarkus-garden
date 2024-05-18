
export default function ConfigLayout(
{
    children
}: {
    children: React.ReactNode;
})

{

    return (
        <section className="flex items-center justify-center">
            <div className="inline-block max-w-lg text-center justify-center">
                {children}
            </div>
        </section>
    );

}

