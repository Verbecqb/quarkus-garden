
export default function ConfigLayout({
                                         children,
                                     }: {
    children: React.ReactNode;
}) {
    return (
        <section className="flex flex-col items-center">
                {children}
        </section>
    );
}

