// src/index.ts
import { registerPlugin } from '@capacitor/core';
import type { CompassHeadingPlugin } from './definitions';

const CompassHeading = registerPlugin<CompassHeadingPlugin>('CompassHeading');
export * from './definitions';
export { CompassHeading };
