CREATE TABLE user_subscriptions (
  channel_id INT8 NOT NULL REFERENCES app_user,
  subscriber_id INT8 NOT NULL REFERENCES app_user,
  PRIMARY KEY (channel_id, subscriber_id)
)