# Kinmin Tracker — Salesperson App (Android)

Native Kotlin + Jetpack Compose app for the Kinmin field-sales team.
This is the **salesperson phone app**. The admin dashboard stays on the web
(your Vercel site), wired to the same Supabase backend.

## Why native (not the web app)
The phone app needs background GPS every 5 minutes, camera capture with a
timestamp/GPS watermark, and full offline support — none of which a web page
can do reliably. The admin side is fine on the web.

## Architecture
Multi-module Gradle + Clean Architecture, built so a feature can be added or
removed without touching the others:

- `:app` — thin shell: the DI graph + the single NavHost that wires features.
- `:core:*` — shared infrastructure (no feature knows another):
  `common, model, designsystem, navigation, network, database, datastore,
   sync, location, camera`.
- `:feature:*` — one self-contained module per feature, depending only on
  `:core`. Features never import each other; they reference routes through
  `:core:navigation`. Each feature defines a repository **interface** (domain)
  with a Hilt-bound **implementation** (data) — Dependency Inversion.

**Add a feature:** new `:feature:x` module + one line in `KinminNavHost`.
**Remove a feature:** delete the module + delete its one line. Nothing else breaks.

## Tech
Kotlin, Compose, MVVM (StateFlow + Coroutines), Hilt (DI), Room (offline queue),
WorkManager (background sync), CameraX (capture), FusedLocation (GPS),
Supabase (Auth + Postgres + Storage + Realtime), Google Maps SDK (admin side).

## Offline-first flow
Every action (order / activity / remark / location ping) is written to Room
with status `PENDING` first, then `SyncWorker` uploads photos to Supabase
Storage and rows to Postgres when there is network, flipping them to `SYNCED`.
The UI never waits on the network.

---

## Setup in Android Studio

1. **Open** the project folder in Android Studio (Giraffe/Koala or newer).
2. On first open, let it **sync Gradle** — it will download the Gradle
   distribution named in `gradle/wrapper/gradle-wrapper.properties`.
   (The wrapper `.jar` is not bundled. If you want to build from the terminal,
   run `gradle wrapper` once in the project root first; Android Studio itself
   doesn't need it.)
3. **Credentials:** copy `local.properties.template` to `local.properties`
   and fill in:
   ```
   SUPABASE_URL=https://YOUR-PROJECT.supabase.co
   SUPABASE_ANON_KEY=YOUR-ANON-PUBLIC-KEY
   MAPS_API_KEY=YOUR-GOOGLE-MAPS-ANDROID-KEY
   ```
   `sdk.dir` is set automatically by Android Studio.
4. **Wire Supabase config at startup.** `SupabaseConfigHolder.config` must be
   set before the Supabase client is first used. The simplest place is in
   `KinminApp.onCreate()` in the `:app` module:
   ```kotlin
   SupabaseConfigHolder.config = SupabaseConfig(
       url = BuildConfig.SUPABASE_URL,
       anonKey = BuildConfig.SUPABASE_ANON_KEY
   )
   ```
   (Keeps `:core:network` independent of the app's BuildConfig.)
5. **Run** on a device/emulator with Google Play services. Grant location and
   camera permissions when asked. For background location, also enable
   "Allow all the time" in app settings.

## Supabase backend (create once)

SQL tables (snake_case columns to match the DTOs):

- `profiles(id uuid pk = auth.uid, name text, role text, route_name text)`
- `orders(id bigserial pk, user_id uuid, party_name text, amount numeric,
   photo_url text, created_at int8)`
- `activities(id bigserial pk, user_id uuid, title text, note text,
   photo_url text, created_at int8)`
- `remarks(id bigserial pk, user_id uuid, text text, created_at int8)`
- `location_pings(id bigserial pk, user_id uuid, lat float8, lng float8,
   recorded_at int8)`

Storage:
- Create a bucket named **`kinmin-photos`** (public, or private + signed URLs).

Auth & security:
- Enable email/password auth; create one user per salesperson + admin.
- Add a `profiles` row per user with the correct `role`
  (`SALESPERSON` or `ADMIN`).
- Turn on **RLS**; restrict each salesperson to their own rows; allow admins
  to read all.

Housekeeping (free-tier friendly):
- A `pg_cron` job to delete rows + storage objects older than 60 days.
- A keep-alive ping (e.g. GitHub Action) so the free project doesn't pause
  after 7 days idle.

## Notes / things to verify on first build
- Library versions in `gradle/libs.versions.toml` are recent known-good pins.
  Bump them to the latest stable when you sync.
- The `supabase-kt` query DSL (`select { filter { eq(...) } }`,
  `storage.upload(...)`) can change between SDK versions — adjust the few
  calls in `AuthRepositoryImpl` and `SyncWorker` if your synced version
  differs. These are clearly commented.
- This is a complete, structured scaffold. It has not been compiled inside the
  environment it was generated in, so expect minor IDE-guided fixes on the
  first Gradle sync.
