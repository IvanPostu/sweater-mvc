--CREATE EXTENSION IF NOT EXISTS pgcrypto;
--
--UPDATE app_user SET password = crypt(password, gen_salt('bf', 8));