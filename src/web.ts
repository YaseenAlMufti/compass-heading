import { WebPlugin } from '@capacitor/core';

import type { CompassHeadingPlugin } from './definitions';

export class CompassHeadingWeb extends WebPlugin implements CompassHeadingPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
