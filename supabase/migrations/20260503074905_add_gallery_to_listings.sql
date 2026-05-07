-- 1. Видаляємо стару колонку (якщо вона дійсно була в базі, IF EXISTS вбереже від помилки, якщо її там не було)
ALTER TABLE apartment_listings
DROP COLUMN IF EXISTS image_url;

-- 2. Додаємо нову колонку для масиву фотографій
ALTER TABLE apartment_listings
    ADD COLUMN image_urls JSONB DEFAULT '[]'::jsonb;

-- 3. Створюємо індекс для швидкої роботи з JSONB (корисно на майбутнє)
CREATE INDEX idx_apartment_listings_image_urls ON apartment_listings USING GIN (image_urls);