# @yaseenalmufti/compass-heading

Accurate real-time compass heading (magnetic) for Capacitor apps.

This plugin uses native motion sensors (magnetometer + accelerometer) to deliver smooth and precise heading data without GPS dependency.

---

## Features

- ✅ Real-time heading updates (0–360°)
- ✅ Real-time compass accuracy updates
- ✅ Native support for Android and iOS
- ✅ No GPS required (just location permission)
- ✅ Works even when standing still (no movement needed)

---

## Installation

```bash
npm install @yaseenalmufti/compass-heading
npx cap sync
```

---

## iOS Setup

Add this to your `Info.plist`:

```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>Used to determine device orientation and heading.</string>
```

---

## Android Setup

In `AndroidManifest.xml`, add:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

Also request location permission at runtime in your app:

```ts
import { Permissions } from '@capacitor/permissions';
await Permissions.request({ name: 'geolocation' });
```

---

## Usage

```ts
import { CompassHeading } from '@yaseenalmufti/compass-heading';

// Start heading updates (true north optional)
await CompassHeading.start();

// Listen for heading changes
CompassHeading.addListener('headingChange', (data) => {
  console.log('Heading:', data.heading); // in degrees (0–360)
  console.log('Accuracy:', data.accuracy); // 0 = low, 1 = medium, 2 = high
});

// Stop when done
await CompassHeading.stop();
```

---

## API

#### `start(): Promise<void>`
Start compass updates.

#### `stop(): Promise<void>`
Stop compass updates.

#### `addListener('headingChange', listener: (data: { heading: number; accuracy: number; }) => void)`
Subscribe to heading updates.

#### `removeAllListeners(): Promise<void>`
Unsubscribe from all listeners.

---

## License

MIT License © [Yaseen Al Mufti](mailto:yaseenalmufti@gmail.com)

