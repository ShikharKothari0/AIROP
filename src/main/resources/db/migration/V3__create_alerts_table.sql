CREATE TABLE alerts (
                        id UUID PRIMARY KEY,
                        source VARCHAR(255) NOT NULL,
                        service VARCHAR(255) NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        description TEXT NOT NULL,
                        severity VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP NOT NULL
);