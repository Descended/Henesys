package henesys.connection.netty;

import henesys.ServerConfig;
import henesys.ServerConstants;
import henesys.client.Client;
import henesys.client.User;
import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.handlers.Handler;
import henesys.handlers.header.InHeader;
import henesys.util.Util;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by Tim on 2/28/2017.
 */
public class ChannelHandler extends SimpleChannelInboundHandler<InPacket> {

    private static final org.apache.logging.log4j.Logger log = LogManager.getRootLogger();
    private static final Map<InHeader, Method> handlers = new HashMap<>();

    public static void initHandlers(boolean mayOverride) {
        long start = System.currentTimeMillis();
        String handlersDir = ServerConstants.HANDLERS_DIR;
        Set<File> files = new HashSet<>();
        Util.findAllFilesInDirectory(files, new File(handlersDir));
        for (File file : files) {
            try {
                // grab all files in the handlers dir, strip them to their package name, and remove .java extension
                String className = file.getPath()
                        .replaceAll("[\\\\|/]", ".")
                        .split("src\\.main\\.java\\.")[1]
                        .replaceAll("\\.java", "");
                Class<?> clazz = Class.forName(className);
                for (Method method : clazz.getMethods()) {
                    Handler handler = method.getAnnotation(Handler.class);
                    if (handler != null) {
                        InHeader header = handler.op();
                        if (header != InHeader.NO) {
                            if (handlers.containsKey(header) && !mayOverride) {
                                throw new IllegalArgumentException(String.format("Multiple handlers found for header %s! " +
                                        "Had method %s, but also found %s.", header, handlers.get(header).getName(), method.getName()));
                            }
                            handlers.put(header, method);
                        }
                        InHeader[] headers = handler.ops();
                        for (InHeader h : headers) {
                            handlers.put(h, method);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                log.error("Error initializing handlers.", e);
            }
        }
        log.info("Initialized {} handlers in {}ms.", handlers.size(), (System.currentTimeMillis() - start));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("[ChannelHandler] | Channel inactive.");

        Client c = (Client) ctx.channel().attr(NettyClient.CLIENT_KEY).get();
        User user = c.getUser();
        Char chr = c.getChr();

        if (chr != null){
            if (!chr.isChangingChannel() && !chr.isInCashShop()) {
                chr.logout();
            } else {
                chr.setChangingChannel(false);
            }
        } else if (user != null) {
            user.unstuck();
        } else {
            log.warn("[ChannelHandler] | Was not able to save character, data inconsistency may have occurred.");
        }

        NettyClient o = ctx.channel().attr(NettyClient.CLIENT_KEY).get();
        if (o != null) {
            o.close();
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, InPacket inPacket) {
        Client c = (Client) ctx.channel().attr(NettyClient.CLIENT_KEY).get();
        Char chr = c.getChr();
        short op = inPacket.decodeShort();
        InHeader inHeader = InHeader.getInHeaderByOp(op);
        if (inHeader == null) {
            handleUnknown(inPacket, op);
            return;
        }
        if (!InHeader.isSpamHeader(InHeader.getInHeaderByOp(op))) {
            if(ServerConfig.DEBUG_MODE){
                log.debug(String.format("[In]\t| %s, %d/0x%s\t| %s", InHeader.getInHeaderByOp(op), op, Integer.toHexString(op).toUpperCase(), inPacket));
            }
        }
        Method method = handlers.get(inHeader);
        try {
            if (method == null) {
                handleUnknown(inPacket, op);
            } else {
                Class<?> clazz = method.getParameterTypes()[0];
                try {
                    if (method.getParameterTypes().length == 3) {
                        method.invoke(this, chr, inPacket, inHeader);
                    } else if (clazz == Client.class) {
                        method.invoke(this, c, inPacket);
                    } else if (clazz == Char.class) {
                        method.invoke(this, chr, inPacket);
                    } else {
                        log.error("Unhandled first param type of handler {}, type = {}", method.getName(), clazz);
                    }
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("Error invoking handler {} for header {}", method.getName(), inHeader, e);
                }
            }
        }
        finally {
            inPacket.release();
        }
    }


    private void handleUnknown(InPacket inPacket, short opCode) {
        if (!InHeader.isSpamHeader(InHeader.getInHeaderByOp(opCode))) {
            if(ServerConfig.DEBUG_MODE){
                log.warn(String.format("Unhandled opcode %s/0x%s, packet %s", opCode, Integer.toHexString(opCode).toUpperCase(), inPacket));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof IOException) {
            log.info("Client forcibly closed the game.");
        } else {
            log.error("Error in channel handler.", cause);
        }
    }
}
