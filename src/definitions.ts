export interface CompassHeadingPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
