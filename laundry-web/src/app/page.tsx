"use client";

import React from "react";
import StatsCard from "@/components/StatsCard";
import { 
  Users, 
  ShoppingCart, 
  Wallet, 
  Clock, 
  CheckCircle2, 
  AlertCircle,
  MoreVertical,
  ChevronRight
} from "lucide-react";
import { motion } from "framer-motion";
import { cn } from "@/lib/utils";

export default function Dashboard() {
  return (
    <div className="space-y-8">
      {/* Header Section */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-white tracking-tight">
            Selamat Datang, <span className="text-blue-400">Admin</span> 👋
          </h1>
          <p className="text-gray-400 mt-1 text-sm">Berikut adalah ringkasan operasional laundry Anda hari ini.</p>
        </div>
        <div className="flex items-center space-x-3">
          <button className="px-4 py-2 bg-white/5 border border-white/10 rounded-xl text-sm font-medium text-white hover:bg-white/10 transition-colors">
            Download Laporan
          </button>
          <button className="px-4 py-2 bg-blue-600 rounded-xl text-sm font-medium text-white hover:bg-blue-700 shadow-lg shadow-blue-600/20 transition-all flex items-center">
            <ShoppingCart className="w-4 h-4 mr-2" />
            Order Baru
          </button>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatsCard 
          title="Total Pelanggan" 
          value="1,284" 
          icon={Users} 
          trend="+12% bulan ini" 
          trendUp={true} 
          color="blue" 
        />
        <StatsCard 
          title="Order Aktif" 
          value="45" 
          icon={ShoppingCart} 
          trend="8 dalam antrian" 
          trendUp={false} 
          color="violet" 
        />
        <StatsCard 
          title="Pendapatan (Mei)" 
          value="Rp 12,4M" 
          icon={Wallet} 
          trend="+5.4% dari April" 
          trendUp={true} 
          color="emerald" 
        />
        <StatsCard 
          title="Estimasi Selesai" 
          value="18" 
          icon={Clock} 
          color="amber" 
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Recent Orders Table */}
        <motion.div 
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          className="lg:col-span-2 glass-card overflow-hidden"
        >
          <div className="p-6 flex items-center justify-between border-b border-white/5">
            <h2 className="text-lg font-bold text-white">Order Terbaru</h2>
            <button className="text-sm text-blue-400 hover:text-blue-300 font-medium flex items-center">
              Lihat Semua <ChevronRight className="w-4 h-4 ml-1" />
            </button>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr className="bg-white/2">
                  <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">Pelanggan</th>
                  <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">Layanan</th>
                  <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">Status</th>
                  <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider">Total</th>
                  <th className="px-6 py-4 text-xs font-semibold text-gray-400 uppercase tracking-wider"></th>
                </tr>
              </thead>
              <tbody className="divide-y divide-white/5">
                {[
                  { id: "ORD-001", name: "Budi Santoso", service: "Cuci Kering + Setrika", status: "Proses", amount: "Rp 35.000", color: "blue" },
                  { id: "ORD-002", name: "Siti Aminah", service: "Bedcover King", status: "Selesai", amount: "Rp 65.000", color: "emerald" },
                  { id: "ORD-003", name: "Andi Wijaya", service: "Express 6 Jam", status: "Antri", amount: "Rp 45.000", color: "amber" },
                  { id: "ORD-004", name: "Lestari", service: "Cuci Lipat", status: "Proses", amount: "Rp 25.000", color: "blue" },
                  { id: "ORD-005", name: "Rizky", service: "Setrika Saja", status: "Diambil", amount: "Rp 15.000", color: "indigo" },
                ].map((order, i) => (
                  <tr key={i} className="hover:bg-white/2 transition-colors group">
                    <td className="px-6 py-4">
                      <div className="flex items-center">
                        <div className="w-8 h-8 rounded-full bg-blue-500/10 flex items-center justify-center text-blue-400 font-bold text-xs mr-3">
                          {order.name.charAt(0)}
                        </div>
                        <div>
                          <p className="text-sm font-medium text-white">{order.name}</p>
                          <p className="text-xs text-gray-500">{order.id}</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <p className="text-sm text-gray-300">{order.service}</p>
                    </td>
                    <td className="px-6 py-4">
                      <span className={cn(
                        "px-2.5 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider border",
                        order.status === 'Selesai' && "bg-emerald-500/10 text-emerald-400 border-emerald-500/20",
                        order.status === 'Proses' && "bg-blue-500/10 text-blue-400 border-blue-500/20",
                        order.status === 'Antri' && "bg-amber-500/10 text-amber-400 border-amber-500/20",
                        order.status === 'Diambil' && "bg-gray-500/10 text-gray-400 border-gray-500/20",
                      )}>
                        {order.status}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <p className="text-sm font-bold text-white">{order.amount}</p>
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
        </motion.div>

        {/* System Health / Quick Actions */}
        <motion.div 
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          className="space-y-6"
        >
          <div className="glass-card p-6">
            <h2 className="text-lg font-bold text-white mb-6">Status Sistem</h2>
            <div className="space-y-4">
              <div className="flex items-center justify-between p-3 rounded-xl bg-white/5 border border-white/5">
                <div className="flex items-center">
                  <div className="w-2 h-2 bg-emerald-500 rounded-full mr-3 animate-pulse"></div>
                  <span className="text-sm text-gray-300">Database MySQL</span>
                </div>
                <CheckCircle2 className="w-4 h-4 text-emerald-500" />
              </div>
              <div className="flex items-center justify-between p-3 rounded-xl bg-white/5 border border-white/5">
                <div className="flex items-center">
                  <div className="w-2 h-2 bg-emerald-500 rounded-full mr-3 animate-pulse"></div>
                  <span className="text-sm text-gray-300">Server Backend</span>
                </div>
                <CheckCircle2 className="w-4 h-4 text-emerald-500" />
              </div>
              <div className="flex items-center justify-between p-3 rounded-xl bg-red-500/10 border border-red-500/20">
                <div className="flex items-center">
                  <div className="w-2 h-2 bg-red-500 rounded-full mr-3"></div>
                  <span className="text-sm text-red-400">Printer Thermal</span>
                </div>
                <AlertCircle className="w-4 h-4 text-red-400" />
              </div>
            </div>
          </div>

          <div className="glass-card p-6 bg-gradient-to-br from-blue-600/20 to-violet-600/20 border-blue-500/20">
            <h3 className="text-white font-bold mb-2">Upgrade Pro Plan</h3>
            <p className="text-sm text-blue-200/70 mb-4">Dapatkan fitur laporan keuangan otomatis dan multi-cabang.</p>
            <button className="w-full py-2.5 bg-blue-500 hover:bg-blue-400 text-white rounded-xl text-sm font-bold transition-all shadow-lg shadow-blue-500/20">
              Pelajari Selengkapnya
            </button>
          </div>
        </motion.div>
      </div>
    </div>
  );
}
