export interface CompassHeadingPlugin {
  start(options?: { useTrueNorth?: boolean }): Promise<void>;
  stop(): Promise<void>;
  addListener(
    eventName: 'headingChange',
    listenerFunc: (data: { heading: number }) => void
  ): Promise<void>;
  removeAllListeners(): Promise<void>;
}
