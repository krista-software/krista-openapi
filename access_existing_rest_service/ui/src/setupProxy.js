const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'https://extension.qa-in.eng.krista.app/extension/api/P7GbgaJMppghB7Rnn4tx1Q_e_e/rest/client',
      changeOrigin: true,
    })
  );
};
