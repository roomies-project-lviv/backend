CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chat_id UUID REFERENCES chats(id) ON DELETE CASCADE,
    sender_id UUID REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,  -- текст повідомлення
    is_read BOOLEAN DEFAULT FALSE, -- чи прочитане повідомлення
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);