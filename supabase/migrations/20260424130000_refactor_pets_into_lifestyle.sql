-- 1. Переносимо дані про улюбленців у JSONB колонку lifestyle_flags
-- Використовуємо COALESCE, щоб уникнути помилок, якщо lifestyle_flags = NULL
UPDATE users u
SET lifestyle_flags = COALESCE(u.lifestyle_flags, '{}'::jsonb) || jsonb_build_object('pet', pt.name)
    FROM pet_types pt
WHERE u.pet_type_id = pt.id;

-- 2. Видаляємо зовнішній ключ та саму колонку з таблиці users
-- Зверни увагу: назва констрейнту може відрізнятися, але за замовчуванням Supabase/Postgres генерує саме таку
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_pet_type_id_fkey;
ALTER TABLE users DROP COLUMN IF EXISTS pet_type_id;

-- 3. Безповоротно видаляємо таблицю pet_types
DROP TABLE IF EXISTS pet_types;