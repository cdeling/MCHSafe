var Pusher = require('pusher');

var pusher = new Pusher({
  appId: '930400',
  key: 'adf7ac7a5c51dcc45eb4',
  secret: '37d5ffc0b7bb89c4446c',
  cluster: 'us2',
  encrypted: true
});

pusher.trigger('my-channel', 'my-event', {
  "message": "hello world"
});
