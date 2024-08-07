package umc.unimade.global.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveFCMToken(String socialId, String fcmToken) {
        redisTemplate.opsForValue().set(socialId, fcmToken);
        redisTemplate.expire(socialId, 30, TimeUnit.DAYS);
    }
    public String getFCMToken(String socialId) {
        Object tokenObj = redisTemplate.opsForValue().get(socialId);
        if (tokenObj != null) {
            return (String) tokenObj;
        } else {
            return null;
        }
    }
    public void removeFCMToken(String socialId) {
        redisTemplate.delete(socialId);
    }
}
