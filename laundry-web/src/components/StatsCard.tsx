"use client";

import React from "react";
import { cn } from "@/lib/utils";
import { LucideIcon, TrendingUp, TrendingDown } from "lucide-react";
import { motion } from "framer-motion";

interface StatsCardProps {
  title: string;
  value: string;
  icon: LucideIcon;
  trend?: string;
  trendUp?: boolean;
  color: "blue" | "violet" | "emerald" | "amber";
}

const colorMap = {
  blue: "from-blue-500/20 to-blue-600/5 text-blue-400 border-blue-500/20",
  violet: "from-violet-500/20 to-violet-600/5 text-violet-400 border-violet-500/20",
  emerald: "from-emerald-500/20 to-emerald-600/5 text-emerald-400 border-emerald-500/20",
  amber: "from-amber-500/20 to-amber-600/5 text-amber-400 border-amber-500/20",
};

const iconBgMap = {
  blue: "bg-blue-500/20 text-blue-400",
  violet: "bg-violet-500/20 text-violet-400",
  emerald: "bg-emerald-500/20 text-emerald-400",
  amber: "bg-amber-500/20 text-amber-400",
};

export default function StatsCard({ title, value, icon: Icon, trend, trendUp, color }: StatsCardProps) {
  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      whileHover={{ y: -5 }}
      transition={{ duration: 0.3 }}
      className={cn(
        "glass-card p-6 border-l-4",
        color === 'blue' && 'border-l-blue-500',
        color === 'violet' && 'border-l-violet-500',
        color === 'emerald' && 'border-l-emerald-500',
        color === 'amber' && 'border-l-amber-500',
      )}
    >
      <div className="flex justify-between items-start mb-4">
        <div className={cn("p-3 rounded-2xl", iconBgMap[color])}>
          <Icon className="w-6 h-6" />
        </div>
        {trend && (
          <div className={cn(
            "flex items-center text-xs font-medium px-2 py-1 rounded-full",
            trendUp ? "bg-emerald-500/10 text-emerald-400" : "bg-red-500/10 text-red-400"
          )}>
            {trendUp ? <TrendingUp className="w-3 h-3 mr-1" /> : <TrendingDown className="w-3 h-3 mr-1" />}
            {trend}
          </div>
        )}
      </div>
      <div>
        <h3 className="text-gray-400 text-sm font-medium mb-1">{title}</h3>
        <p className="text-3xl font-bold text-white tracking-tight">{value}</p>
      </div>
    </motion.div>
  );
}
