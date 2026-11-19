# 🛠️ PunctureWala 24x7 — On-Demand Mobile Tyre & Puncture Service App

A mobile-first single-page app (SPA) for an on-demand roadside tyre & puncture repair service in India.
Built with **HTML + Tailwind CSS + Vanilla JS**. No build step required — open `index.html` directly.

---

## 📱 Screens

| Screen | Description |
|---|---|
| **मैप / बुकिंग** | Home map, service selection, vehicle type, live fare calculator & van booking |
| **टायर स्टोर** | On-van tyre inventory — buy new/second-hand tyres with on-spot fitting |
| **रेट कार्ड** | Transparent pricing — distance-based visit fee + per-service charges |
| **हेल्पलाइन** | 24x7 emergency helpline modal |
| **ट्रैकिंग** | Live van tracking screen with ETA, mechanic profile & fare summary |

## 💡 Features

- **Dynamic Fare Calculator** — base visit fee (₹150) + ₹15/km beyond 2 km + service cost
- **VIP Superfast Toggle** — queue-jump priority option (+₹150)
- **Van Busy/Free Status** — header toggle simulates mechanic availability with countdown
- **Vehicle Type Selector** — Bike / Car / SUV / Commercial with per-vehicle pricing
- **Booking → Tracking Flow** — seamless screen transition with booking summary
- **Tyre Store** — order new/second-hand tyres with mobile van delivery + fitting
- **Demo Mode** — "वैन को नज़दीक लाएं" button simulates arrival animation

## 🗂️ Project Structure

```
puncturewala-24x7/
├── index.html      ← Complete single-file app
└── README.md       ← This file
```

## 🚀 Getting Started

Just open `index.html` in any modern browser. No server or build tools needed.

## 🔧 Planned Improvements

- [ ] OTP-based login screen
- [ ] Booking history / past orders
- [ ] Mechanic / shop-owner dashboard panel
- [ ] Real GPS integration via browser Geolocation API
- [ ] PWA manifest + service worker for offline support
- [ ] Multi-file split: `style.css`, `app.js`, `data.js`
