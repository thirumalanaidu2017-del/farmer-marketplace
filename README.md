# Farmer Marketplace

Full-stack app: farmer, customer, and admin modules. Customers search a
product (e.g. "rice") and get farmer details + location directly.

- `backend/` — Node.js + Express + MongoDB REST API
- `android/` — Native Java Android app (Retrofit client for the API above)

Start with `backend/README.md` to get the API running, then `android/README.md`
to point the app at it.

## Quick mental model

```
Farmer registers → creates a Farmer profile (unverified)
Admin approves the farmer → verified = true
Farmer adds Listings (product + price + quantity)
Customer searches "rice" → GET /api/search?product=rice
  → returns each verified farmer selling rice, with name, phone,
    location and price — sorted by distance if customer shared location
Customer taps a result → calls the farmer directly
```
