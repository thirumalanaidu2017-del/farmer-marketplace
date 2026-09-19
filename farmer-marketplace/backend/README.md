# Farmer Marketplace — Backend API

Node.js + Express + MongoDB backend for a farmer/customer/admin marketplace app.
Core feature: searching a product (e.g. "rice") returns farmer details and
location directly — no separate product page.

## Setup

1. Install dependencies:
   ```
   npm install
   ```
2. Copy `.env.example` to `.env` and fill in your MongoDB URI and a JWT secret:
   ```
   cp .env.example .env
   ```
   - Local Mongo: `mongodb://localhost:27017/farmer_marketplace`
   - Or use a free MongoDB Atlas cluster and paste its connection string.
3. Run it:
   ```
   npm run dev
   ```
   Server starts on `http://localhost:5000` by default.

## Creating the first admin account

There's no public admin signup (by design). After registering a normal
account, open your Mongo shell / Atlas UI and set that user's `role` field
to `"admin"`:
```js
db.users.updateOne({ email: "you@example.com" }, { $set: { role: "admin" } })
```

## API summary

| Method | Route | Who | Purpose |
|---|---|---|---|
| POST | /api/auth/register | public | Create customer/farmer account |
| POST | /api/auth/login | public | Get a JWT |
| GET | /api/search?product=rice&lat=&lng= | public | **Core feature** — farmers selling a product, sorted by distance |
| GET | /api/farmers/me | farmer | View own profile |
| PUT | /api/farmers/me | farmer | Update farm name / address / location |
| GET | /api/farmers/:id | public | View a farmer's public profile |
| POST | /api/listings | farmer | Create a listing |
| GET | /api/listings/mine | farmer | View own listings |
| PUT | /api/listings/:id | farmer | Update a listing |
| DELETE | /api/listings/:id | farmer | Remove a listing |
| GET | /api/products | public | Browse product catalog |
| GET | /api/admin/farmers/pending | admin | Farmers awaiting verification |
| PUT | /api/admin/farmers/:id/verify | admin | Approve a farmer |
| GET | /api/admin/users | admin | List all users |

## Why search returns farmers, not products

A `Listing` links a `Farmer` to a `Product`. `/api/search` matches the
product name, then returns **one result per farmer** carrying that farmer's
name, phone, location and the listing price — so the customer app can skip
straight to "who's selling this near me" instead of a generic product page.
Only `verified` farmers show up, so admin approval gates visibility.
