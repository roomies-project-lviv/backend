CREATE TABLE apartments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- ідентифікатор квартири
    area DECIMAL, -- площа квартири
    rooms_total INT, -- загальна кількість кімнат
    address VARCHAR(255), -- точна або приблизна адреса
    latitude DECIMAL(9, 6), -- широта
    longitude DECIMAL(9, 6), -- довгота
    description TEXT, -- детальний опис умов
    available_from DATE -- з якого числа можна заселятися
);