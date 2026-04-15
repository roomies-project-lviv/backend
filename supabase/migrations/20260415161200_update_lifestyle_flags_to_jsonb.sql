-- 1. Змінюємо тип колонки на JSONB.
-- USING '{}'::jsonb акуратно замінює старі "зламані" або бітові дані на порожній JSON-об'єкт.
ALTER TABLE users
ALTER COLUMN lifestyle_flags TYPE JSONB USING '{}'::jsonb;

-- 2. Додаємо GIN-індекс для блискавичного пошуку по масивах і ключах (для алгоритму метчингу)
CREATE INDEX idx_user_lifestyle_flags ON users USING GIN (lifestyle_flags);