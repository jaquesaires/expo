import Ajv, { JSONSchemaType } from 'ajv';

export interface PluginConfigType {
  android?: {
    compileSdkVersion?: number;
    targetSdkVersion?: number;
    buildToolsVersion?: string;
    kotlinVersion?: string;
    packagingOptions?: {
      pickFirst?: string[];
      exclude?: string[];
      merge?: string[];
      doNotStrip?: string[];
    };
  };
}

const schema: JSONSchemaType<PluginConfigType> = {
  type: 'object',
  properties: {
    android: {
      type: 'object',
      properties: {
        compileSdkVersion: { type: 'integer', nullable: true },
        targetSdkVersion: { type: 'integer', nullable: true },
        buildToolsVersion: { type: 'string', nullable: true },
        kotlinVersion: { type: 'string', nullable: true },
        packagingOptions: {
          type: 'object',
          properties: {
            pickFirst: { type: 'array', items: { type: 'string' }, nullable: true },
            exclude: { type: 'array', items: { type: 'string' }, nullable: true },
            merge: { type: 'array', items: { type: 'string' }, nullable: true },
            doNotStrip: { type: 'array', items: { type: 'string' }, nullable: true },
          },
          nullable: true,
        },
      },
      nullable: true,
    },
  },
};

export function validateConfig(config: any): PluginConfigType {
  const validate = new Ajv().compile(schema);
  if (!validate(config)) {
    throw new Error('Invalid expo-build-properties config: ' + JSON.stringify(validate.errors));
  }
  return config;
}
