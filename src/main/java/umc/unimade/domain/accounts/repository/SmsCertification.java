package umc.unimade.domain.accounts.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SmsCertification {
    private final String PREFIX = "sms:"; // key값이 중복되지 않도록 상수 선언
    private final int LIMIT_TIME = 100 * 60; // 인증번호 유효 시간

    private final StringRedisTemplate stringRedisTemplate;

    // Redis에 저장
    public void createSmsCertification(String phone, String certificationNumber) {
        stringRedisTemplate.opsForValue()
                .set(PREFIX + phone, certificationNumber, Duration.ofSeconds(LIMIT_TIME));
    }

    // 휴대전화 번호에 해당하는 인증번호 불러오기
    public String getSmsCertification(String phone) {
        return stringRedisTemplate.opsForValue().get(PREFIX + phone);
    }

    // 인증 완료 시, 인증번호 Redis에서 삭제
    public void deleteSmsCertification(String phone) {
        stringRedisTemplate.delete(PREFIX + phone);
    }

    // Redis에 해당 휴대번호로 저장된 인증번호가 존재하는지 확인
    public boolean hasKey(String phone) {
        return stringRedisTemplate.hasKey(PREFIX + phone);
    }
}