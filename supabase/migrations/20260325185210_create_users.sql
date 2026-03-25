CREATE TABLE public.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- ідентифікатор
    email VARCHAR(255) UNIQUE NOT NULL, -- пошта для авторизації
    password VARCHAR(255) NOT NULL, -- пароль
    first_name VARCHAR(100) NOT NULL, -- ім'я
    last_name VARCHAR(100) NOT NULL, -- прізвище
    birth_date DATE, -- дата народження
    gender VARCHAR(30), -- стать
    avatar_url VARCHAR(255), -- посилання на фотографію профілю
    pet_type_id INT REFERENCES pet_types(id) ON DELETE SET NULL, -- зв'язок із довідником тварин (може бути NULL)
    lifestyle_flags BIT(8), -- бітова маска звичок
    sleep_schedule VARCHAR(20), -- людина “Жайворонок” чи “Cова”
    occupation VARCHAR(30), -- рід занять
    guests_frequency VARCHAR(20), -- ставлення до гостей
    noice_tolerance VARCHAR(20), -- толерантність до шуму
    cleanliness_level VARCHAR(20), -- рівень охайності
    dietary_preferences VARCHAR(30), -- харчові вподобання
    bio TEXT, -- короткий опис про себе
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- дата реєстрації
)