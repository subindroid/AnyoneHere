-- 거절 사유 컬럼 추가 (스팟 신청 거절 시 사유를 신청자에게 보여주기 위함)
ALTER TABLE add_spot_applications ADD COLUMN reject_reason VARCHAR(300) NULL;
ALTER TABLE remove_spot_applications ADD COLUMN reject_reason VARCHAR(300) NULL;
