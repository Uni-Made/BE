package umc.unimade.domain.notification.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FcmTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public FcmTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(Long userId, String token) {
        redisTemplate.opsForValue().set(userId.toString(), token);
    }

    public String getToken(Long userId) {
        return redisTemplate.opsForValue().get(userId.toString());
    }

    public void deleteToken(Long userId) {
        redisTemplate.delete(userId.toString());
    }
}