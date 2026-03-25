-- Таблиця фітографій квартир
CREATE TABLE apartment_photos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    apartment_id UUID REFERENCES apartments(id) ON DELETE CASCADE, -- до якої квартири належить фото
    photo_url VARCHAR NOT NULL, -- посилання на фото у хмарному сховищі
    is_main BOOLEAN DEFAULT FALSE, -- чи є це фото головною обкладинкою
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);