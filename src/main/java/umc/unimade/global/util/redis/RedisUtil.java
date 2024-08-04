package umc.unimade.global.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveFCMToken(String email, String fcmToken) {
        redisTemplate.opsForValue().set(email, fcmToken);
        redisTemplate.expire(email, 30, TimeUnit.DAYS);
    }
    public String getFCMToken(String userEmail) {
        Object tokenObj = redisTemplate.opsForValue().get(userEmail);
        if (tokenObj != null) {
            return (String) tokenObj;
        } else {
            return null;
        }
    }
    public void removeFCMToken(String userEmail) {
        redisTemplate.delete(userEmail);
    }
}
