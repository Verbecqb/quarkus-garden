import type { Config } from "tailwindcss";
import {nextui} from "@nextui-org/react";

const config: Config = {
  content: [
   "./node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}", // nextUI
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {},
  },
  darkMode: "class",
  plugins: [nextui({
                defaultTheme: ""
            })
  ],
};
export default config;

// tailwind.config.js


