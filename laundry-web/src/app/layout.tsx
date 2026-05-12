import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Sidebar from "@/components/Sidebar";
import Navbar from "@/components/Navbar";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "LaundryPro - Modern Laundry Management",
  description: "Advanced laundry dashboard with real-time tracking",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${inter.className} bg-background text-foreground antialiased`}>
        <Sidebar />
        <div className="pl-64 pt-16 min-h-screen">
          <Navbar />
          <main className="p-8">
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
