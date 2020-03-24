CREATE TABLE message_likes (
  user_id INT8 NOT NULL REFERENCES app_user,
  message_id INT8 NOT NULL REFERENCES message,
  PRIMARY KEY(user_id, message_id)
)