-- Таблиця оголошень
CREATE TABLE apartment_listings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    author_id UUID REFERENCES users(id) ON DELETE CASCADE, -- хто створив оголошення
    apartment_id UUID REFERENCES apartments(id) ON DELETE CASCADE, -- яку саме квартиру здають
    title VARCHAR NOT NULL, -- заголовок
    price_per_month DECIMAL NOT NULL, -- ціна оренди
    city_active BOOLEAN DEFAULT TRUE, -- статус оголошення
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);