package com.cookienats.common.papimq.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Configuration
public class PapiMQServer implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(PapiMQServer.class);

    private static AtomicBoolean serverStartup = new AtomicBoolean(false);

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Value("${papimq.host:0.0.0.0}")
    private String host;
    @Value("${papimq.port:9539}")
    private Integer port;
    // 内核为此套接口排队的最大连接个数，对于给定的监听套接口，内核要维护两个队列，未链接队列和已连接队列大小总和最大值
    @Value("${papimq.ioThreadNum:5}")
    private Integer ioThreadNum;
    @Value("${papimq.backlog:1024}")
    int backlog;

    @PostConstruct
    public void start() throws InterruptedException {
        logger.info("Begin to Start PapiMq Server!");
        serverStartup.compareAndSet(false, true);

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(ioThreadNum);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, backlog)
                .childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new MQMessageDecoder()).addLast(new MQMessageEncoder())
                                .addLast(new MQMessageHandler());
                    }
                });

        channel = serverBootstrap.bind(this.host, port).sync().channel();
        logger.info("PapiMq Server Startup! Listening port:[{}] Ready for Connections...", port);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }


    @PreDestroy
    public void shutdown(){
        logger.info("Shutdown PapiMq Server, release resources");
        serverStartup.compareAndSet(true, false);

        if (null == channel) {
            logger.error("Server Channel is Null");
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        bossGroup = null;
        workerGroup = null;
        channel = null;
    }

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public Integer getIoThreadNum() {
        return ioThreadNum;
    }
    public void setIoThreadNum(Integer ioThreadNum) {
        this.ioThreadNum = ioThreadNum;
    }
    public int getBacklog() {
        return backlog;
    }
    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }
}
