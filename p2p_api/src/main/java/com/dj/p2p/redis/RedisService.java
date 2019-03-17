package com.dj.p2p.redis;


import org.springframework.data.redis.connection.DataType;
import redis.clients.jedis.BinaryClient;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {

//    ======================================String===========================================
    /**
     * String-set-设置永久缓存
     * @param key 缓存key
     * @param value 缓存值
     * @return true/false
     */
    boolean set(String key, Object value);

    /**
     * String-set-设置临时缓存
     * @param key 缓存key
     * @param value 缓存值
     * @param timeOut 超时时间(单位秒)
     * @return true/false
     */
    boolean set(String key, Object value, long timeOut);

    /**
     * String-get-获取缓存值
     * @param key 缓存Key
     * @return value/null
     */
    <T> T get(String key);

    /**
     * String-自增(long)
     * @param key
     * @param start
     * @return
     */
    Long incr(String key, long start);

    /**
     * String-自增(Double)
     * @param key
     * @param start
     * @return
     */
    Double incr(String key, double start);

    /**
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     * (有序列化问题-暂未解决)
     * @param key
     * @param value
     * @return
     */
    boolean append(String key, String value);

    /**
     * String-set if Not exists
     * @param key
     * @param value
     * @return
     */
    boolean setNX(String key, Object value);
//    ======================================Hash===========================================

    /**
     * Hash-push-设置永久缓存
     * @param key 缓存Key
     * @param hashKey hashKey
     * @param hashValue hashValue
     * @return
     */
    boolean pushHash(String key, String hashKey, Object hashValue);

    /**
     * Hash-push-设置临时缓存
     * @param key
     * @param hashKey
     * @param hashValue
     * @param timeOut
     * @return
     */
    boolean pushHash(String key, String hashKey, Object hashValue, long timeOut);

    /**
     * Hash-pushAll
     * @param key
     * @param hash
     * @return
     */
    boolean pushHashAll(String key, Map<String, Object> hash);

    /**
     * Hash-获取Hash值
     * @param key 缓存Key
     * @param hashKey HashKey
     * @return
     */
    <T> T getHash(String key, String hashKey);

    /**
     * Hash-获取全部HashKey
     * @param key 缓存Key
     * @return
     */
    <T> Set<T> getHashKeys(String key);

    /**
     * Hash-获取全部HashValue
     * @param key 缓存Key
     * @return
     */
    <T> List<T> getHashValues(String key);

    /**
     * Hash-获取全部Hash
     * @param key 缓存Key
     * @return
     */
    <K, V> Map<K, V> getHashALL(String key);

    /**
     * Hash-删除Hash值
     * @param key 缓存Key
     * @param hashKey HashKey
     * @return
     */
    boolean delHash(String key, String hashKey);

//    ======================================List===========================================

    /**
     * List-设置值永久缓存（批量）-向右
     * @param key
     * @param valueList
     * @return 当前List中新增后的长度 >0 表示成功
     */
    long rPush(String key, List<?> valueList);

    /**
     * List-（单个）-向右
     * @param key
     * @param value
     * @return
     */
    long rPush(String key, Object value);

    /**
     * List-插入(批量)-向左
     * @param key
     * @param valueList
     * @return
     */
    long lPush(String key, List<?> valueList);

    /**
     * List-插入(单个)-向左
     * @param key
     * @param value
     * @return
     */
    long lPush(String key, Object value);

    /**
     * List-指定元素插入(未实现)
     * @param key 缓存Key
     * @param where before/after
     * @param pivot 指定元素
     * @param value 插入值
     * @return
     */
    long lInsert(String key, BinaryClient.LIST_POSITION where, Object pivot, Object value);

    /**
     * List-取出最左边的元素(取出后List中该元素消失)
     * @param key 缓存Key
     * @return 元素值
     */
    <T> T lPorp(String key);

    /**
     * List-取出最右边的元素(取出后List中该元素消失)
     * @param key 缓存Key
     * @return 元素值
     */
    <T> T rPorp(String key);

    /**
     * List-返回指定位置元素值
     * @param key 缓存Key
     * @param index 位置
     * @return 值
     */
    <T> T lIndex(String key, int index);

    /**
     * List-修改指定位置元素值
     * @param key 缓存Key
     * @param index 位置
     * @param vaule 新值
     * @return true/false
     */
    boolean lSet(String key, int index, Object vaule);

    /**
     * List-获取List值,顺序：（左->右）
     * -1,-1可获取最右元素, 0,0可获取最最左元素
     * @param key 缓存Key
     * @param startIndex 开始位置 0代表第一个
     * @param endIndex 结束位置 -1代表最后一个
     * @return
     */
    <T> List<T> lRange(String key, int startIndex, int endIndex);

    /**
     * List-获取List中全部值,顺序：（左->右）
     * @param key 缓存Key
     * @return
     */
    <T> List<T> getListAll(String key);


    /**
     * Key-删除缓存
     * @param key 缓存Key
     * @return true/false
     */
    boolean del(String key);

    /**
     * Key-检测对应key是否存在
     * @param key 缓存Key
     * @return exist:true, not exist:false
     */
    boolean checkKeyIsExist(String key);

    /**
     * Key-设置超时时间
     * @param key 缓存Key
     * @return (-1 为永久有效)
     */
    boolean expireKey(String key, long timeOut);

    /**
     * Key-指定时间过期
     * @param key 缓存Key-必须存在
     * @param timeOut 过期日期
     * @return true/false
     */
    boolean expireAt(String key, Date timeOut);

    /**
     * Key-获取Key对应类型
     * @param key
     * @return NONE("none")/STRING("string")/LIST("list")/SET("set")/ZSET("zset")/HASH("hash");
     */
    DataType type(String key);

    /**
     * Key-获取Key剩余存活时间
     * @param key 缓存Key
     * @return (-2/-1/>0: key不存在/永久/剩余存活时间)
     */
    long ttl(String key);

    /**
     * Key-模糊查找Key
     * @param pattern 表达式 (已屏蔽 keys * 命令)
     * @return
     */
    <T> Set<T> keys(String pattern);

    //    ======================================Set===========================================

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    Set<Object> sGet(String key)throws Exception;
    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    boolean sHasKey(String key, Object value)throws Exception;
    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    long sSet(String key, Object... values)throws Exception;
    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    long sGetSetSize(String key) throws Exception;
    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    long setRemove(String key, Object... values) throws Exception;


//    ======================================ZSet===========================================

//    ======================================Key===========================================


}
