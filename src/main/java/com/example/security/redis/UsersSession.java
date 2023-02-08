package com.example.security.redis;

import com.example.security.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(timeToLive = 60 * 60 * 3)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UsersSession {
    private String id;
    private Users user;
}
