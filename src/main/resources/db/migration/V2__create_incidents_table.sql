CREATE TABLE incidents (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                           title VARCHAR(255) NOT NULL,

                           description TEXT,

                           severity VARCHAR(20) NOT NULL,

                           status VARCHAR(20) NOT NULL,

                           created_by UUID,

                           created_at TIMESTAMP NOT NULL,

                           updated_at TIMESTAMP
);