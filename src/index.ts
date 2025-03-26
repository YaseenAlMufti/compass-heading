import { registerPlugin } from '@capacitor/core';

import type { CompassHeadingPlugin } from './definitions';

const CompassHeading = registerPlugin<CompassHeadingPlugin>('CompassHeading', {
  web: () => import('./web').then((m) => new m.CompassHeadingWeb()),
});

export * from './definitions';
export { CompassHeading };
