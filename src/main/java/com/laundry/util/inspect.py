import sys

with open(r"c:\Users\LOQ\Downloads\laundry-dashboard-main\laundry-dashboard-main\src\main\java\com\laundry\ui\MainDashboard.java", "r", encoding="utf-8") as f:
    lines = f.readlines()

for idx in range(197, 212):
    line = lines[idx]
    hex_vals = [hex(ord(c)) for c in line]
    print(f"Line {idx+1}: {repr(line)} -> {hex_vals}")
