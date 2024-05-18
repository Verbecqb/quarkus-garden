"use client"

import {
    Navbar,
    NavbarBrand,
    NavbarContent,
    NavbarItem,
    Link,
    Button,
    DropdownItem,
    DropdownTrigger,
    Dropdown,
    DropdownMenu
} from "@nextui-org/react"

import {
    BeakerIcon,
    CogIcon,
    ChartPieIcon,
} from '@heroicons/react/24/outline';

import { usePathname } from "next/navigation";

const links = [
    {"url": "/map", "name": "Growing map", "description": "View growing map", icon: CogIcon},
    {
        "url": "/config",
        "name": "Garden configuration",
        "description": "Configuration of plant types and garden config",
        icon: CogIcon
    },
    {"url": "/dashboard", "name": "Garden dashboard", "description": "Dashboard", icon: ChartPieIcon}
]

export default function Nav() {

    const pathname = usePathname();

    return (<>
            <Navbar>

                <NavbarBrand>
                    <BeakerIcon className="h-7 w-7 text-black-500"/>
                    <p className="font-bold text-inherit">Robot Garden</p>
                </NavbarBrand>

                <NavbarContent className="hidden sm:flex gap-4" justify="center">

                    {
                        links.map((link) => {
                            const LinkIcon = link.icon
                            return (
                                <NavbarItem key={link.url}>

                                    <Link color="foreground" href={link.url} className={pathname == link.url ? 'underline' : ''}>
                                        <LinkIcon className="h-4 w-4"/>
                                        {link.name}
                                    </Link>
                                </NavbarItem>
                            );
                        })
                    }
                </NavbarContent>

                <NavbarContent justify="end">
                    <NavbarItem className="hidden lg:flex">
                        <Link href="#">Login</Link>
                    </NavbarItem>
                    <NavbarItem>
                        <Button as={Link} color="primary" href="#" variant="flat">
                            Sign Up
                        </Button>
                    </NavbarItem>
                </NavbarContent>
            </Navbar>

        </>
    );
}