CREATE TABLE user_work_schedules (
    user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE, -- зв'язок з користувачем
    schedule_type VARCHAR(20) NOT NULL, -- тип графіка
    base_location VARCHAR(100), -- основний формат роботи для постійного графіка
    even_period_location VARCHAR(100), -- формат роботи у парні дні або тижні
    odd_period_location VARCHAR(100), -- формат роботи у непарні дні або тижні
    week1_office_days INT[], -- масив днів тижня для першого тижня циклу
    week2_office_days INT[], -- масив днів тижня для офісу на другий тиждень
    shift_work_days INT, -- кількість робочих днів поспіль
    shift_rest_days INT -- кількість вихідних днів поспіль
);