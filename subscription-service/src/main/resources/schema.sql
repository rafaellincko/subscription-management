CREATE TABLE IF NOT EXISTS subscriptions (
                               id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               user_id UUID NOT NULL UNIQUE,
                               plan VARCHAR(50) NOT NULL,
                               start_date DATE NOT NULL,
                               expiration_date DATE NOT NULL,
                               status VARCHAR(30) NOT NULL,
                               failed_renewal_attempts INT NOT NULL DEFAULT 0
);