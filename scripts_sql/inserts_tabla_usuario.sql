INSERT INTO Usuario (id, username, email, activo, fechaRegistro, CredencialAcceso_id, eliminado)
SELECT
  n AS id,
  CONCAT('user_', LPAD(n, 5, '0')) AS username,
  CONCAT('user', LPAD(n, 5, '0'), '@mail.com') AS email,
  CASE WHEN RAND() < 0.8 THEN 1 ELSE 0 END AS activo,
  DATE_ADD('2022-01-01', INTERVAL FLOOR(RAND()*730) DAY) AS fechaRegistro,
  n AS CredencialAcceso_id,
  0 AS eliminado
FROM (
  WITH RECURSIVE numeros AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numeros WHERE n < 100
  )
  SELECT n FROM numeros
) AS t;