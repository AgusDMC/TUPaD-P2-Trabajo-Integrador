INSERT INTO CredencialAcceso (id, eliminado, hashPassword, salt, ultimoCambio, requiereReset)
SELECT 
  n AS id,
  0 AS eliminado,
  CONCAT('hash_', LPAD(n, 6, '0')) AS hashPassword,
  CONCAT('salt_', LPAD(n, 5, '0')) AS salt,
  DATE_ADD('2020-01-01', INTERVAL FLOOR(RAND()*1825) DAY)
    + INTERVAL FLOOR(RAND()*24) HOUR
    + INTERVAL FLOOR(RAND()*60) MINUTE
    + INTERVAL FLOOR(RAND()*60) SECOND AS ultimoCambio,
  CASE WHEN RAND() < 0.1 THEN 1 ELSE 0 END AS requiereReset
FROM (
  WITH RECURSIVE numeros AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numeros WHERE n < 100
  )
  SELECT n FROM numeros
) AS t;