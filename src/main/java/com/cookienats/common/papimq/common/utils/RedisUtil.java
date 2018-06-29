package com.cookienats.common.papimq.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.*;

@Component
public class RedisUtil {
    private Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    //redis分布式锁默认db-10
    private static final Integer lockDbIndex = 10;

    @Autowired
    JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 获取所有满足条件的key(慎用,低效)
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Set<String> keys = new HashSet<String>();
            keys.addAll(jedis.keys(pattern));
            return keys;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 使用SCAN方式获取满足条件key
     * @param pattern
     * @return
     */
    public Set<String> keyScan(String pattern){
        Jedis jedis = null;
        try {
            Set<String> result = new HashSet<>();
            String SCAN_POINTER_START = "0";
            ScanParams scanParams = new ScanParams().count(10).match(pattern);
            String cur = SCAN_POINTER_START;
            do {
                ScanResult<String> scanResult = jedis.scan(cur, scanParams);
                result.addAll(scanResult.getResult());
                cur = scanResult.getStringCursor();
            } while (!cur.equals(SCAN_POINTER_START));
            return result;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            returnResource(jedis);
        }
    }

    /**
     * 设置k-v多少秒后过期
     * @param key
     * @param seconds
     * @return
     */
    public boolean expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.expire(key.getBytes(), seconds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    public void set(String key, String value){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    public void set(byte[] key, byte[] value){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    public Boolean setNX(byte[] key, byte[] value){
        Boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.setnx(key, value) == 1? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public boolean setNX(String key, String value,long time) {
        final String LOCK_SUCCESS = "OK";
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if(LOCK_SUCCESS.equals(jedis.set(key, value,"NX","PX",time))){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
        return false;
    }

    /**
     * 设置k-v
     * @param key
     * @param value
     */
    public void setObject(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key.getBytes(), KryoUtil.writeObjectToByteArray(value));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 设置k-v
     * @param key
     * @param value
     * @return
     */
    public void setObject(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 批量设置k-v
     * @param map
     * @return
     */
    public void batchSet(Map<String, Object> map) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Pipeline pipeline = jedis.pipelined();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                pipeline.set(entry.getKey().getBytes(), KryoUtil.writeObjectToByteArray(entry.getValue()));
            }
            pipeline.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 获取值
     * @param key
     * @param typeToken
     * @return
     */
    public <T> T get(String key, T typeToken) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return (T)KryoUtil.readObjectFromByteArray(jedis.get(key.getBytes()), typeToken.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /**
     * @功能: 删除k-v
     * @param key
     * @return
     */
    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(key.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 删除多组k-v
     * @param keys
     * @return
     */
    public void del(String... keys) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(keys);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    public Long llen(String key){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.llen(key.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public void rPush(String key, Object value) {
        byte[] arr = KryoUtil.writeObjectToByteArray(value);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.rpush(key.getBytes(), arr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    public byte[] lGet(String key, Long index) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lindex(key.getBytes(), index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /** ----------------------------------------- Queue(FIFO) ------------------------------------------- */

    /**
     * @功能: 入队
     * @param key
     * @param values
     * @return
     */
    public void EnQueues(String key, Object... values) {
        int len = values.length;
        byte[][] strings = new byte[len][];
        for (int i = 0; i < len; i++) {
            strings[i] = KryoUtil.writeObjectToByteArray(values[i]);
        }

        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.rpush(key.getBytes(), strings);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    public void EnQueue(String key, Object value) {
        byte[] arr = KryoUtil.writeObjectToByteArray(value);
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.rpush(key.getBytes(), arr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    public void EnQueue(String key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.rpush(key.getBytes(), value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 出队
     * @param key
     * @param typeToken
     * @return
     */
    public <T> T DeQueue(String key, T typeToken) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return (T)KryoUtil.readObjectFromByteArray(jedis.lpop(key.getBytes()), typeToken.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /** ----------------------------------------- Stack(FILO) ------------------------------------------- */

    /**
     * @功能: 压栈
     * @param key
     * @param values
     * @return
     */
    public void pushStack(String key, Object... values) {
        int len = values.length;
        byte[][] strings = new byte[len][];
        for (int i = 0; i < len; i++) {
            strings[i] = KryoUtil.writeObjectToByteArray(values[i]);
        }

        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.rpush(key.getBytes(), strings);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 出栈
     * @param key
     * @param typeToken
     * @return
     */
    public <T> T popStack(String key, T typeToken) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return (T)KryoUtil.readObjectFromByteArray(jedis.rpop(key.getBytes()), typeToken.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /** ----------------------------------------- Hash ------------------------------------------- */

    /**
     * @功能: 设置hashmap
     * @param key
     * @param map
     * @return
     */
    public void putHashMap(String key, Map<byte[], byte[]> map) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hmset(key.getBytes(), map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 获取hashmap中的值
     * @param key
     * @param fields
     * @return
     */
    public List<byte[]> getHashMap(String key, byte[] field) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hmget(key.getBytes(), field);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /**
     * @功能: 获取hashmap中所有的key
     * @param key
     * @return
     */
    public Set<String> getHashMapKeys(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hkeys(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /**
     * @功能: 获取hashmap中所有的value
     * @param key
     * @return
     */
    public List<byte[]> getHashMapValues(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hvals(key.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    public byte[] hget(String key, byte[] field){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hget(key.getBytes(), field);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            returnResource(jedis);
        }
        return null;
    }

    public String hget(String key, String field){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hget(key, field);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            returnResource(jedis);
        }
        return null;
    }

    public void hset(String key, byte[] field, byte[] value){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hset(key.getBytes(), field, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            returnResource(jedis);
        }
    }

    public void hset(String key, String field, String value){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hset(key, field, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            returnResource(jedis);
        }
    }

    public void hdel(String key, String field){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hdel(key, field);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            returnResource(jedis);
        }
    }

    public void hdel(String key, byte[] field){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hdel(key.getBytes(), field);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            returnResource(jedis);
        }
    }

    public void hsetnx(String key, byte[] field, byte[] value){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hsetnx(key.getBytes(), field, value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            returnResource(jedis);
        }
    }

    public boolean hexist(String key, String field){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hexists(key, field);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            returnResource(jedis);
        }
        return false;
    }

    public boolean hexist(String key, byte[] field){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hexists(key.getBytes(), field);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            returnResource(jedis);
        }
        return false;
    }

    /** ----------------------------------------- SortedSet ------------------------------------------- */

    /**
     * @功能: 设置SortedSet
     * @param key
     * @param map
     * @return
     */
    public void zadd(String key, Map<byte[], Double> map) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.zadd(key.getBytes(), map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 为member的score加上增量increment
     * @param key
     * @param increment
     * @param member
     * @return
     */
    public void zincrby(String key, double increment, byte[] member) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.zincrby(key.getBytes(), increment, member);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 删除下标为start到end的记录(包含start和stop)
     * @param key
     * @param start 从0开始,-1表示最后一个成员,-2表示倒数第二个成员,以此类推
     * @param end
     * @return
     */
    public void zremrangeByRank(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.zremrangeByRank(key.getBytes(), start, end);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 按倒序取下标为start到end的记录(包含start和stop)
     * @param key
     * @param start 从0开始,-1表示最后一个成员,-2表示倒数第二个成员,以此类推
     * @param end
     * @return
     */
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrevrangeWithScores(key.getBytes(), start, end);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /**
     * @功能: 按倒序取SortedSet中对应key和member的下标
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrevrank(key.getBytes(), member.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /** ----------------------------------------- Pub/Sub ------------------------------------------- */

    /**
     * @功能: 从指定通道订阅消息(jedis的subscribe是阻塞的,所以需要通过异步启动)
     * @param jedisPubSub
     * @param channel
     * @return
     */
    public void subscribe(JedisPubSub jedisPubSub, String channel) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.subscribe(jedisPubSub, channel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * @功能: 取消订阅
     * @param jedisPubSub
     */
    public void unsubscribe(JedisPubSub jedisPubSub) {
        jedisPubSub.unsubscribe();
    }

    /**
     * @功能: 指定通道发送消息
     * @param channel
     * @param message
     * @return
     */
    public void publish(String channel, String message) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.publish(channel, message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
    }


    /**
     * 尝试获取分布式锁     默认使用redis db-10
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        final String LOCK_SUCCESS = "OK";
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(lockDbIndex);
            if(LOCK_SUCCESS.equals(jedis.set(lockKey, requestId,"NX","PX",expireTime))){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
        return false;
    }

    /**
     * 释放分布式锁       默认使用redis db-10
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        final Long RELEASE_SUCCESS = 1L;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(lockDbIndex);
            //其实很简单，首先获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）。
            //那么为什么要使用Lua语言来实现呢？因为要确保上述操作是原子性的
            //eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，并且直到eval命令执行完成
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            returnResource(jedis);
        }
        return false;
    }

}
