package com.dj.p2p.redis;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;

import java.util.*;
import java.util.concurrent.TimeUnit;


//@org.springframework.stereotype.Service
@Service
public class RedisServiceImpl implements RedisService {

    /** 日志 */
    private static Logger logger = LogManager.getLogger(RedisServiceImpl.class.getName());

    @Autowired
    private RedisTemplate redisTemplate;

    //    ======================================String===========================================

    @Override
    public boolean set(String key, Object value) {
        try {
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, value);
            logger.info("Redis String set {"+key+"} success.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Redis String set {"+key+"} error "+ e);
            return false;
        }
    }

    @Override
    public boolean set(String key, Object value, long timeOut) {
        try {
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, value, timeOut, TimeUnit.SECONDS);
            logger.info("Redis String set {"+ key+"}, timeout:{"+timeOut+"} /s success.");
            return true;
        } catch (Exception e) {
            logger.error("Redis String set {"+key+"} fail:{"+e+"}");
            return false;
        }
    }

    @Override
    public <T> T get(String key) {
        try {
            ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
            T result = valueOperations.get(key);
            logger.info("Redis String get {"+ key +"} success.");
            return result;
        } catch (Exception e) {
            logger.error("Redis String get {"+ key +"} fail:{"+e+"}");
            return null;
        }
    }

    @Override
    public Long incr(String key, long start) {
        try{
            ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
            Long result = valueOperations.increment(key, start);
            return result;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public Double incr(String key, double start) {
        try{
            ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
            Double result = valueOperations.increment(key, start);
            return result;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean append(String key, String value) {
//        try{
//            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//            valueOperations.append(key, new String(redisTemplate.getDefaultSerializer().serialize(value)));
//            return true;
//        } catch (Exception e){
//            return false;
//        }
        return false;
    }

    @Override
    public boolean setNX(String key, Object value) {
        try {
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            boolean result = valueOperations.setIfAbsent(key, value);
            return result;
        } catch (Exception e){
            return false;
        }

    }
//    ======================================Hash===========================================

    @Override
    public boolean pushHash(String key, String hashKey, Object hashValue) {
        try {
            HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
            hashOperations.put(key, hashKey, hashValue);
            logger.info("Redis Hash push [{"+key+"}-{"+hashKey+"}] success.");
            return true;
        } catch (Exception e) {
            logger.error("Redis Hash push [{"+key+"}-{"+hashKey+"}] fail:{"+e+"}");
            return false;
        }
    }

    @Override
    public boolean pushHash(String key, String hashKey, Object hashValue, long timeOut) {
        try {
            HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
            hashOperations.put(key, hashKey, hashValue);
            redisTemplate.expireAt(key, new Date(System.currentTimeMillis() + (timeOut * 1000)));
            logger.info("Redis Hash push [{"+key+"}-{"+hashKey+"}] timeout:{"+timeOut+"}/s success.");
            return true;
        } catch (Exception e) {
            logger.error("Redis Hash push [{"+key+"}-{"+hashKey+"}] fail:{"+e+"}");
            return false;
        }
    }

    @Override
    public boolean pushHashAll(String key, Map<String, Object> hash) {
        try {
            HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
            hashOperations.putAll(key, hash);
            logger.info("Redis Hash pushALL {"+key+"} success.");
            return true;
        } catch (Exception e){
            logger.error("Redis Hash pushALL [{"+key+"}-{"+hash+"}] fail:{"+e+"}");
            return false;
        }
    }

    @Override
    public <T> T getHash(String key, String hashKey) {
        try {
            HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
            T result = hashOperations.get(key, hashKey);
            logger.info("Redis Hash getHash [{"+key+"}-{"+hashKey+"}] success.");
            return result;
        } catch (Exception e) {
            logger.error("Redis Hash getHash [{"+key+"}-{"+hashKey+"}] fail:{"+e+"}");
            return null;
        }
    }

    @Override
    public <T> Set<T> getHashKeys(String key) {
        try {
            HashOperations<String, T, ?> hashOperations = redisTemplate.opsForHash();
            Set<T> result = hashOperations.keys(key);
            logger.info("Redis Hash getHashKeys [{"+key+"}] success.");
            return result;
        } catch (Exception e) {
            logger.error("Redis Hash getHashKeys [{"+key+"}] fail:{"+e+"}");
            return null;
        }
    }

    @Override
    public <T> List<T> getHashValues(String key) {
        try {
            HashOperations<String, ?, T> hashOperations = redisTemplate.opsForHash();
            List<T> result = hashOperations.values(key);
            logger.info("Redis Hash getHashValues [{"+key+"}] success.");
            return result;
        } catch (Exception e) {
            logger.error("Redis Hash getHashValues [{"+key+"}] fail:{"+e+"}");
            return null;
        }
    }

    @Override
    public <K, V> Map<K, V> getHashALL(String key) {
        try {
            HashOperations<String, K, V> hashOperations = redisTemplate.opsForHash();
            Cursor<Map.Entry<K, V>> curosr = hashOperations.scan(key, ScanOptions.NONE);
            Map<K, V> result = new HashMap<>();
            while (curosr.hasNext()){
                Map.Entry<K, V> entry = curosr.next();
                result.put(entry.getKey(), entry.getValue());
            }
            logger.info("Redis Hash getHashALL [{"+key+"}] success.");
            return result;
        } catch (Exception e) {
            logger.error("Redis Hash getHashALL [{"+key+"}] fail:{"+e+"}");
            return null;
        }
    }

    @Override
    public boolean delHash(String key, String hashKey) {
        HashOperations<String, String, ?> hashOperations = redisTemplate.opsForHash();
        try {
            hashOperations.delete(key, hashKey);// 删除key存在返回1 不存在返回0
            logger.info("Redis Hash delHash [{"+key+"}-{"+hashKey+"}] success.");
            return true;
        } catch (Exception e){
            logger.error("Redis Hash delHash [{"+key+"}-{"+hashKey+"}] fail:{"+e+"}");
            return false;
        }
    }

    @Override
    public long rPush(String key, List<?> valueList) {
        try {
            ListOperations<String, Object> listListOperations = redisTemplate.opsForList();
            long index = listListOperations.rightPushAll(key, valueList.toArray());
            return index;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public long rPush(String key, Object value) {
        try {
            ListOperations<String, Object> listListOperations = redisTemplate.opsForList();
            long index = listListOperations.rightPush(key, value);
            return index;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public long lPush(String key, List<?> valueList) {
        try {
            ListOperations<String, Object> listListOperations = redisTemplate.opsForList();
            long index = listListOperations.leftPushAll(key, valueList.toArray());
            return index;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public long lPush(String key, Object value) {
        try {
            ListOperations<String, Object> listListOperations = redisTemplate.opsForList();
            long index = listListOperations.leftPush(key, value);
            return index;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public long lInsert(String key, BinaryClient.LIST_POSITION where, Object pivot, Object value) {
        return 0;
    }

    @Override
    public <T> List<T> lRange(String key, int startIndex, int endIndex) {
        try {
            ListOperations<String, T> listListOperations = redisTemplate.opsForList();
            List<T> list = listListOperations.range(key, startIndex, endIndex);
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> List<T> getListAll(String key) {
        return lRange(key, 0, -1);
    }

    @Override
    public <T> T lPorp(String key) {
        try {
            ListOperations<String, T> listListOperations = redisTemplate.opsForList();
            T result = listListOperations.leftPop(key);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> T rPorp(String key) {
        try {
            ListOperations<String, T> listListOperations = redisTemplate.opsForList();
            T result = listListOperations.rightPop(key);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> T lIndex(String key, int index) {
        try {
            ListOperations<String, T> listListOperations = redisTemplate.opsForList();
            T result = listListOperations.index(key, index);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean lSet(String key, int index, Object vaule) {
        try {
            ListOperations<String, Object> listListOperations = redisTemplate.opsForList();
            listListOperations.set(key, index, vaule);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean del(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean checkKeyIsExist(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean expireKey(String key, long timeOut) {
        try {
            boolean flag = redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
            return flag;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean expireAt(String key, Date timeOut) {
        try {
            boolean flag = false;
            // 检查Key是否存在
            if (checkKeyIsExist(key)) {
                flag = redisTemplate.expireAt(key, timeOut);
            }
            return flag;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public DataType type(String key) {
        try {
            if (checkKeyIsExist(key)) {
                return redisTemplate.type(key);
            }
        } catch (Exception e) {
            return DataType.NONE;
        }
        return DataType.NONE;
    }

    @Override
    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public <T> Set<T> keys(String pattern) {
        if (pattern.equals("*")){
            return null;
        }
        return redisTemplate.keys(pattern);
    }

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key,Object value){
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object...values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object ...values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
