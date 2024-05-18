import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "@/app/ui/globals.css";
import {Providers} from "./providers";
import Nav from "@/app/ui/nav";
import clsx from "clsx";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Robot Garden - Control board",
  description: "Control my robot garden",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
      <html lang="en" className='light'>

      <body className={clsx("min-h-screen bg-background font-sans")}>

      <Providers>

          <div className="flex h-screen flex-col md:flex-row md:overflow-hidden">
              <div className="w-full content-center">
                  <Nav/>
                  <div className="w-full">{children}</div>
              </div>

          </div>


      </Providers>
      </body>
      </html>
  );
}
