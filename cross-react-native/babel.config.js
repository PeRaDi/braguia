module.exports = {
  presets: ['module:@react-native/babel-preset'],
  plugins: [
    [
      '@babel/plugin-proposal-decorators',
      {
        legacy: true,
      },
    ],
    [
      'module-resolver',
      {
        extensions: [
          '.js',
          '.jsx',
          '.ts',
          '.tsx',
          '.json',
          '.ios.js',
          '.android.js',
          '.ios.jsx',
          '.android.jsx',
        ],
        root: ['.'],
        alias: {
          '@home': './src/home',
          '@pins': './src/pins',
          '@settings': './src/settings',
          '@trails': './src/trails',
          '@store': './src/store',
          '@shared': './src/shared',
          '@model': './src/model',
          '@src': './src',
          '@contacts': './src/contacts',
        },
      },
    ],
  ],
};
