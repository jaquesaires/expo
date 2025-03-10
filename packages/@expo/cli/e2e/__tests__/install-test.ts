/* eslint-env jest */
import JsonFile from '@expo/json-file';
import execa from 'execa';
import fs from 'fs/promises';
import klawSync from 'klaw-sync';
import path from 'path';

import {
  execute,
  projectRoot,
  getLoadedModulesAsync,
  bin,
  setupTestProjectAsync,
  installAsync,
} from './utils';

const originalForceColor = process.env.FORCE_COLOR;
const originalCI = process.env.CI;
beforeAll(async () => {
  await fs.mkdir(projectRoot, { recursive: true });
  process.env.FORCE_COLOR = '0';
  process.env.CI = '1';
});
afterAll(() => {
  process.env.FORCE_COLOR = originalForceColor;
  process.env.CI = originalCI;
});

it('loads expected modules by default', async () => {
  const modules = await getLoadedModulesAsync(`require('../../build/src/install').expoInstall`);
  expect(modules).toStrictEqual([
    '../node_modules/ansi-styles/index.js',
    '../node_modules/arg/index.js',
    '../node_modules/chalk/source/index.js',
    '../node_modules/chalk/source/util.js',
    '../node_modules/has-flag/index.js',
    '../node_modules/supports-color/index.js',
    '@expo/cli/build/src/install/index.js',
    '@expo/cli/build/src/log.js',
    '@expo/cli/build/src/utils/args.js',
  ]);
});

it('runs `npx install install --help`', async () => {
  const results = await execute('install', '--help');
  expect(results.stdout).toMatchInlineSnapshot(`
    "
      Description
        Install a module or other package to a project

      Usage
        $ npx expo install [packages...] [options]

      Options
        --check     Check which installed packages need to be updated.
        --fix       Automatically update any invalid package versions.
        --npm       Use npm to install dependencies. Default when package-lock.json exists
        --yarn      Use Yarn to install dependencies. Default when yarn.lock exists
        -h, --help  Output usage information

      Additional options can be passed to the underlying install command by using --
        $ expo install react -- --verbose
        > yarn add react --verbose
        "
  `);
});

it(
  'runs `npx expo install expo-sms`',
  async () => {
    const projectRoot = await setupTestProjectAsync('basic-install', 'with-blank');
    // `npx expo install expo-sms`
    await execa('node', [bin, 'install', 'expo-sms'], { cwd: projectRoot });

    // List output files with sizes for snapshotting.
    // This is to make sure that any changes to the output are intentional.
    // Posix path formatting is used to make paths the same across OSes.
    const files = klawSync(projectRoot)
      .map((entry) => {
        if (entry.path.includes('node_modules') || !entry.stats.isFile()) {
          return null;
        }
        return path.posix.relative(projectRoot, entry.path);
      })
      .filter(Boolean);

    const pkg = await JsonFile.readAsync(path.resolve(projectRoot, 'package.json'));

    // Added expected package
    expect(pkg.dependencies['expo-sms']).toBe('~10.1.0');
    expect(pkg.devDependencies).toEqual({
      '@babel/core': '^7.12.9',
    });

    // Added new packages
    expect(Object.keys(pkg.dependencies).sort()).toStrictEqual([
      'expo',
      'expo-sms',
      'react',
      'react-native',
    ]);

    expect(files).toStrictEqual(['App.js', 'app.json', 'package.json', 'yarn.lock']);
  },
  // Could take 45s depending on how fast npm installs
  60 * 1000
);

it(
  'runs `npx expo install --check` fails',
  async () => {
    const projectRoot = await setupTestProjectAsync('install-check-fail', 'with-blank');
    await installAsync(projectRoot, ['add', 'expo-sms@1.0.0', 'expo-auth-session@1.0.0']);

    let pkg = await JsonFile.readAsync(path.resolve(projectRoot, 'package.json'));
    // Added expected package
    expect(pkg.dependencies['expo-sms']).toBe('1.0.0');

    try {
      await execa('node', [bin, 'install', '--check'], { cwd: projectRoot });
      throw new Error('SHOULD NOT HAPPEN');
    } catch (error) {
      expect(error.stderr).toMatch(/expo-auth-session@1\.0\.0 - expected version: ~3\.5\.0/);
      expect(error.stderr).toMatch(/expo-sms@1\.0\.0 - expected version: ~10\.1\.0/);
      expect(error.stderr).toMatch(
        /npx expo install expo-auth-session@~3\.5\.0 expo-sms@~10\.1\.0/
      );
    }

    await expect(
      execa('node', [bin, 'install', 'expo-sms', '--check'], { cwd: projectRoot })
    ).rejects.toThrowError(/expo-sms@1\.0\.0 - expected version: ~10\.1\.0/);

    // Check doesn't fix packages
    pkg = await JsonFile.readAsync(path.resolve(projectRoot, 'package.json'));
    // Added expected package
    expect(pkg.dependencies['expo-sms']).toBe('1.0.0');
  },
  // Could take 45s depending on how fast npm installs
  60 * 1000
);

it(
  'runs `npx expo install --fix` fails',
  async () => {
    const projectRoot = await setupTestProjectAsync('install-fix-fail', 'with-blank');
    await installAsync(projectRoot, ['add', 'expo-sms@1.0.0', 'expo-auth-session@1.0.0']);

    await execa('node', [bin, 'install', '--fix', 'expo-sms'], { cwd: projectRoot });

    // Ensure the versions are invalid
    await expect(
      execa('node', [bin, 'install', '--check'], { cwd: projectRoot })
    ).rejects.toThrow();

    // Check doesn't fix packages
    let pkg = await JsonFile.readAsync(path.resolve(projectRoot, 'package.json'));
    // Added expected package
    expect(pkg.dependencies['expo-sms']).toBe('~10.1.0');

    // Didn't fix expo-auth-session since we didn't pass it in
    expect(pkg.dependencies['expo-auth-session']).toBe('1.0.0');

    // Fix all versions
    await execa('node', [bin, 'install', '--fix'], { cwd: projectRoot });

    // Check that the versions are fixed
    pkg = await JsonFile.readAsync(path.resolve(projectRoot, 'package.json'));

    // Didn't fix expo-auth-session since we didn't pass it in
    expect(pkg.dependencies['expo-auth-session']).toBe('~3.5.0');
  },
  // Could take 45s depending on how fast npm installs
  60 * 1000
);
