"use client"; // Ensure it's a client component

import { Geist, Geist_Mono } from "next/font/google";
import { Provider } from "react-redux";
import store from "../lib/Redux/store"; // Make sure this is the correct path to your Redux store
import "./globals.css";


export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        
        <Provider store={store}>
          {children}
        </Provider>
      </body>
    </html>
  );
}
