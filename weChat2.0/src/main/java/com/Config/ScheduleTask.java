package com.Config;

import com.Utils.Constant;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
@Log4j2
public class ScheduleTask {

    public final static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Qualifier("redisTemplateString")
    @Autowired
    private RedisTemplate<String, String> redisTemplateString;

    @Scheduled(fixedRate = 1000 * 60)
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        try {
            if(redisTemplateString.hasKey(Constant.REDIS_HEART_BEAT_STATE)){
                Constant.REDIS_IS_DOWN = false;
            }else{
                redisTemplateString.opsForValue().set(Constant.REDIS_HEART_BEAT_STATE, "1");
                Constant.REDIS_IS_DOWN = false;
            }
            logger.info("redis无异常!");
        }catch (Exception e){
            if(e instanceof RedisConnectionFailureException){
                Constant.REDIS_IS_DOWN = true;
                logger.error("redis已宕机!");
            }
        }
    }
}
