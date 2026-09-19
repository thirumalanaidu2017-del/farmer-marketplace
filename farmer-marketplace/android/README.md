# Farmer Marketplace — Android App (Java)

Talks to the backend in `../backend`. Three flows, gated by the `role`
returned at login/register: `customer`, `farmer`, `admin`.

## Open the project

1. Open Android Studio → **Open** → select this `android/` folder.
2. Let Gradle sync (it will pull Retrofit, Maps, Location, RecyclerView, etc.
   from the dependencies already listed in `app/build.gradle`).
3. Run on an emulator or device (`minSdk 24`).

## Connecting to your backend

`app/src/main/java/.../api/ApiClient.java` has:
```java
private static final String BASE_URL = "http://10.0.2.2:5000/";
```
- `10.0.2.2` is the special address the **Android emulator** uses to reach
  `localhost` on your dev machine — keep it as-is if you're running the
  backend locally and testing on the emulator.
- For a **real device**, replace it with your machine's LAN IP (e.g.
  `http://192.168.1.20:5000/`) or your deployed API's URL once you host it.

## Google Maps API key

`AndroidManifest.xml` has a placeholder:
```xml
<meta-data android:name="com.google.android.geo.API_KEY" android:value="YOUR_GOOGLE_MAPS_API_KEY" />
```
Get a free key from the Google Cloud Console (enable "Maps SDK for
Android") and drop it in. The profile screen (`FarmerProfileActivity`) has
a `TODO` where you can drop a `MapView`/`MapFragment` marker using the
farmer's `location.getLatitude()/getLongitude()`.

## What's wired up

- **Login/Register** — role picker (customer/farmer), JWT stored in
  SharedPreferences via `SessionManager`, auto-routes to the right dashboard.
- **Customer → SearchActivity** — the core feature. Type a product name,
  get a list of farmers with price, quantity, address, and distance (if
  location permission is granted). Tap a result → `FarmerProfileActivity`
  with a Call button.
- **Farmer → FarmerDashboardActivity** — shows verification status, lists
  own listings, "+ Add Listing" → `AddListingActivity`.
- **Admin → AdminDashboardActivity** — approve pending farmers so their
  listings become searchable.

## What's intentionally left as a next step

- Map pin rendering on `FarmerProfileActivity` (marked with `TODO`) —
  needs your Maps API key first.
- Editing/deleting a farmer's own listing from the UI (backend routes for
  it already exist: `PUT`/`DELETE /api/listings/:id`).
- Image uploads for produce photos.
- Push notifications, order/checkout flow (this app is a discovery/contact
  marketplace — customers call the farmer directly — extend into a full
  order pipeline if you need in-app transactions).
