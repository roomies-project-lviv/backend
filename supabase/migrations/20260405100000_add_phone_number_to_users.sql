ALTER TABLE public.users
    ADD COLUMN phone_number VARCHAR(20);


ALTER TABLE public.users
    ADD CONSTRAINT phone_number_check
        CHECK (phone_number ~ '^\+380[0-9]{9}$');