CREATE TABLE outbox_events (
                               id UUID PRIMARY KEY,
                               aggregate_id UUID NOT NULL,
                               topic VARCHAR(255) NOT NULL,
                               event_key VARCHAR(255) NOT NULL,
                               payload TEXT NOT NULL,
                               status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
                               attempts INTEGER NOT NULL DEFAULT 0,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               published_at TIMESTAMP NULL,
                               last_error TEXT NULL
);

CREATE INDEX idx_outbox_events_pending
    ON outbox_events (status, created_at);