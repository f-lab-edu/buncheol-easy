-- Test H2 Database용 users 테이블 생성
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    social_id        VARCHAR(100)  NOT NULL,
    nickname         VARCHAR(10)  NOT NULL,
    email            VARCHAR(320) NOT NULL,
    phone_number     VARCHAR(30)  NULL,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMP    NULL,

    -- H2에서는 가상 컬럼을 GENERATED ALWAYS AS로 생성
    active_social_id VARCHAR(64) GENERATED ALWAYS AS (CASE WHEN deleted_at IS NULL THEN social_id ELSE NULL END),

    PRIMARY KEY (id)
);

-- 가상 컬럼에 유니크 인덱스
CREATE UNIQUE INDEX idx_users_active_social_id ON users(active_social_id);

-- social_id 인덱스
CREATE INDEX idx_users_social_id ON users(social_id);
