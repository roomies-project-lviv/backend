-- Оголошення про пошук співмешканця
CREATE TABLE roommate_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE, -- хто шукає
    target_city_id INT REFERENCES cities(id), -- нормалізоване місто пошуку
    budget_max DECIMAL NOT NULL, -- максимальний бюджет на людину
    move_in_date DATE, -- бажана дата переїзду
    requirements TEXT, -- побажання до майбутнього співмешканця
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);