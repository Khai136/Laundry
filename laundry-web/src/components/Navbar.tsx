"use client";

import React from "react";
import { Search, Bell, User } from "lucide-react";

export default function Navbar() {
  return (
    <nav className="fixed top-0 right-0 left-64 z-30 h-16 glass-nav flex items-center justify-between px-8">
      <div className="relative w-96">
        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <Search className="h-4 w-4 text-gray-400" />
        </div>
        <input
          type="text"
          className="block w-full pl-10 pr-3 py-2 border border-white/10 rounded-xl bg-white/5 text-gray-300 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500/50 focus:border-transparent transition-all text-sm"
          placeholder="Cari transaksi atau pelanggan..."
        />
      </div>

      <div className="flex items-center space-x-5">
        <button className="relative p-2 text-gray-400 hover:text-white transition-colors">
          <Bell className="w-5 h-5" />
          <span className="absolute top-1 right-1 w-2 h-2 bg-blue-500 rounded-full border-2 border-[#0a0a0b]"></span>
        </button>
        
        <div className="h-8 w-px bg-white/10 mx-2"></div>
        
        <div className="flex items-center space-x-3 cursor-pointer group">
          <div className="text-right">
            <p className="text-sm font-semibold text-white group-hover:text-blue-400 transition-colors">Admin Laundry</p>
            <p className="text-xs text-gray-500">Super Administrator</p>
          </div>
          <div className="w-10 h-10 rounded-full border border-white/10 bg-white/5 flex items-center justify-center group-hover:border-blue-500/50 transition-all overflow-hidden">
             <div className="bg-gradient-to-tr from-blue-500 to-indigo-600 w-full h-full flex items-center justify-center">
                <User className="text-white w-6 h-6" />
             </div>
          </div>
        </div>
      </div>
    </nav>
  );
}
