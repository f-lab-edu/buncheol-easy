-- users 테이블 생성
CREATE TABLE IF NOT EXISTS users
(
    id           BIGINT           NOT NULL AUTO_INCREMENT COMMENT 'PK',
    social_id    VARCHAR(100)      NOT NULL COMMENT '소셜 고유 ID',
    nickname     VARCHAR(10)      NOT NULL COMMENT '닉네임',
    email        VARCHAR(320)     NOT NULL COMMENT '이메일',
    phone_number VARCHAR(30)      NULL     COMMENT '전화번호',
    created_at   TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at   TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    deleted_at   TIMESTAMP        NULL     COMMENT '삭제일시',

    -- 삭제되지 않았을 때만 social_id를 가지고, 삭제되면 NULL이 되는 가상 컬럼
    active_social_id VARCHAR(64) AS (IF(deleted_at IS NULL, social_id, NULL)) VIRTUAL,

    PRIMARY KEY (id),

    -- 가상 컬럼에 유니크 인덱스 부여
    UNIQUE INDEX idx_users_active_social_id (active_social_id),

    -- social_id 인덱스 추가
    INDEX idx_users_social_id (social_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
