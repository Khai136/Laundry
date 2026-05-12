"use client";

import React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { 
  LayoutDashboard, 
  Users, 
  Package, 
  ShoppingCart, 
  CreditCard, 
  BarChart3, 
  UserCog,
  LogOut
} from "lucide-react";

const menuItems = [
  { name: "Dashboard", href: "/", icon: LayoutDashboard },
  { name: "Data Pelanggan", href: "/customers", icon: Users },
  { name: "Paket Layanan", href: "/packages", icon: Package },
  { name: "Cucian Masuk", href: "/orders", icon: ShoppingCart },
  { name: "Transaksi", href: "/payments", icon: CreditCard },
  { name: "Laporan", href: "/reports", icon: BarChart3 },
  { name: "Manajemen User", href: "/users", icon: UserCog },
];

export default function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="fixed left-0 top-0 z-40 h-screen w-64 glass border-r border-white/10 transition-transform sm:translate-x-0">
      <div className="h-full flex flex-col px-4 py-6 overflow-y-auto">
        <div className="flex items-center mb-10 px-2">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-500 to-violet-600 flex items-center justify-center mr-3 shadow-lg shadow-blue-500/20">
            <ShoppingCart className="text-white w-6 h-6" />
          </div>
          <span className="text-xl font-bold tracking-tight text-white">LAUNDRY<span className="text-blue-400">PRO</span></span>
        </div>

        <nav className="flex-1 space-y-1">
          {menuItems.map((item) => {
            const isActive = pathname === item.href;
            return (
              <Link
                key={item.name}
                href={item.href}
                className={cn(
                  "flex items-center px-4 py-3 text-sm font-medium rounded-xl transition-all duration-200 group",
                  isActive 
                    ? "bg-blue-600/20 text-blue-400 border border-blue-500/20 shadow-[0_0_15px_rgba(59,130,246,0.1)]" 
                    : "text-gray-400 hover:bg-white/5 hover:text-white"
                )}
              >
                <item.icon className={cn(
                  "w-5 h-5 mr-3 transition-colors",
                  isActive ? "text-blue-400" : "text-gray-400 group-hover:text-white"
                )} />
                {item.name}
              </Link>
            );
          })}
        </nav>

        <div className="mt-auto pt-6 border-t border-white/10">
          <button className="flex items-center w-full px-4 py-3 text-sm font-medium text-red-400 rounded-xl hover:bg-red-500/10 transition-colors">
            <LogOut className="w-5 h-5 mr-3" />
            Logout
          </button>
        </div>
      </div>
    </aside>
  );
}
