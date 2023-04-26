module.exports = {
  apps: [
    {
      name: 'backend',
      cwd: './packages/backend',
      script: 'dist/main.js',
      watch: true,
      exec_mode: 'cluster',
    },
    {
      name: 'web',
      script: './.output/server/index.mjs',
      cwd: './packages/web',
      watch: true,
      exec_mode: 'cluster',
      env: {
        port: 80
      }
    }
  ],
};
