CREATE TABLE chats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(20) NOT NULL, -- тип чату ('direct' або 'group')
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);