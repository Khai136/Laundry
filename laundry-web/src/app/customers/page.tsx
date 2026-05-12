"use client";

import React, { useState } from "react";
import { 
  Users, 
  Plus, 
  Search, 
  Filter, 
  MoreVertical, 
  Mail, 
  Phone, 
  MapPin,
  ChevronLeft,
  ChevronRight
} from "lucide-react";
import { motion } from "framer-motion";
import { cn } from "@/lib/utils";

const customers = [
  { id: "CUST-001", name: "Budi Santoso", email: "budi@example.com", phone: "0812-3456-7890", address: "Jl. Merdeka No. 12, Jakarta", totalOrders: 15, status: "Aktif" },
  { id: "CUST-002", name: "Siti Aminah", email: "siti@example.com", phone: "0856-9876-5432", address: "Jl. Mawar No. 5, Bandung", totalOrders: 8, status: "Aktif" },
  { id: "CUST-003", name: "Andi Wijaya", email: "andi@example.com", phone: "0813-1122-3344", address: "Jl. Melati No. 45, Surabaya", totalOrders: 22, status: "Aktif" },
  { id: "CUST-004", name: "Lestari Putri", email: "lestari@example.com", phone: "0878-5566-7788", address: "Jl. Anggrek No. 8, Medan", totalOrders: 3, status: "Baru" },
  { id: "CUST-005", name: "Rizky Ramadhan", email: "rizky@example.com", phone: "0821-9988-7766", address: "Jl. Kamboja No. 21, Yogyakarta", totalOrders: 0, status: "Non-Aktif" },
];

export default function CustomersPage() {
  const [searchTerm, setSearchTerm] = useState("");

  return (
    <div className="space-y-8">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-white tracking-tight">Data Pelanggan</h1>
          <p className="text-gray-400 mt-1 text-sm">Kelola informasi pelanggan dan riwayat transaksi mereka.</p>
        </div>
        <button className="px-4 py-2 bg-blue-600 rounded-xl text-sm font-medium text-white hover:bg-blue-700 shadow-lg shadow-blue-600/20 transition-all flex items-center w-fit">
          <Plus className="w-4 h-4 mr-2" />
          Tambah Pelanggan
        </button>
      </div>

      <div className="glass-card p-4 flex flex-col md:flex-row gap-4 items-center justify-between">
        <div className="relative w-full md:w-96">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
          <input 
            type="text" 
            placeholder="Cari nama, email, atau ID..." 
            className="w-full pl-10 pr-4 py-2 bg-white/5 border border-white/10 rounded-xl text-sm text-white focus:outline-none focus:ring-2 focus:ring-blue-500/50"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div className="flex items-center gap-2 w-full md:w-auto">
          <button className="flex-1 md:flex-none flex items-center justify-center px-4 py-2 bg-white/5 border border-white/10 rounded-xl text-sm text-gray-300 hover:text-white hover:bg-white/10 transition-colors">
            <Filter className="w-4 h-4 mr-2" />
            Filter
          </button>
          <button className="flex-1 md:flex-none flex items-center justify-center px-4 py-2 bg-white/5 border border-white/10 rounded-xl text-sm text-gray-300 hover:text-white hover:bg-white/10 transition-colors">
            Export CSV
          </button>
        </div>
      </div>

      <motion.div 
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="glass-card overflow-hidden"
      >
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="bg-white/2 border-b border-white/5">
                <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">Info Pelanggan</th>
                <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">Kontak</th>
                <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">Alamat</th>
                <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">Total Order</th>
                <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">Status</th>
                <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider"></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/5">
              {customers.map((cust, i) => (
                <tr key={i} className="hover:bg-white/2 transition-colors group">
                  <td className="px-6 py-4">
                    <div className="flex items-center">
                      <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-500/20 to-indigo-500/20 flex items-center justify-center text-blue-400 font-bold text-sm mr-3 border border-blue-500/20">
                        {cust.name.charAt(0)}
                      </div>
                      <div>
                        <p className="text-sm font-bold text-white">{cust.name}</p>
                        <p className="text-xs text-gray-500">{cust.id}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="space-y-1">
                      <div className="flex items-center text-xs text-gray-300">
                        <Mail className="w-3 h-3 mr-2 text-blue-400" />
                        {cust.email}
                      </div>
                      <div className="flex items-center text-xs text-gray-300">
                        <Phone className="w-3 h-3 mr-2 text-blue-400" />
                        {cust.phone}
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-start text-xs text-gray-300 max-w-[200px]">
                      <MapPin className="w-3 h-3 mr-2 mt-0.5 text-blue-400 shrink-0" />
                      <span className="truncate-2-lines">{cust.address}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="text-sm font-medium text-white">{cust.totalOrders} Order</div>
                  </td>
                  <td className="px-6 py-4">
                    <span className={cn(
                      "px-2.5 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider border",
                      cust.status === 'Aktif' && "bg-emerald-500/10 text-emerald-400 border-emerald-500/20",
                      cust.status === 'Baru' && "bg-blue-500/10 text-blue-400 border-blue-500/20",
                      cust.status === 'Non-Aktif' && "bg-red-500/10 text-red-400 border-red-500/20",
                    )}>
                      {cust.status}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <button className="p-1 text-gray-500 hover:text-white transition-colors">
                      <MoreVertical className="w-4 h-4" />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="p-4 border-t border-white/5 flex items-center justify-between">
          <p className="text-xs text-gray-500">Menampilkan 1-5 dari 1,284 pelanggan</p>
          <div className="flex items-center gap-2">
            <button className="p-2 text-gray-500 hover:text-white disabled:opacity-50" disabled>
              <ChevronLeft className="w-4 h-4" />
            </button>
            <button className="w-8 h-8 flex items-center justify-center rounded-lg bg-blue-600 text-white text-xs font-bold">1</button>
            <button className="w-8 h-8 flex items-center justify-center rounded-lg hover:bg-white/5 text-gray-400 text-xs font-bold">2</button>
            <button className="w-8 h-8 flex items-center justify-center rounded-lg hover:bg-white/5 text-gray-400 text-xs font-bold">3</button>
            <button className="p-2 text-gray-500 hover:text-white">
              <ChevronRight className="w-4 h-4" />
            </button>
          </div>
        </div>
      </motion.div>
    </div>
  );
}
