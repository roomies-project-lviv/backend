CREATE TABLE user_social_links (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- ідентифікатор посилання
    user_id UUID REFERENCES users(id) ON DELETE CASCADE, -- зв'язок з користувачем
    platform_name VARCHAR(50) NOT NULL, -- назва платформи
    url VARCHAR(255) NOT NULL -- посилання на профіль
);