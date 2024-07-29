package com.ggghost.framework.utlis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-19
 * @Description: ip工具类
 * @Version: 1.0
 */
public class IpAddrUtils {

    private static final Logger log = LoggerFactory.getLogger(IpAddrUtils.class);

    private static final String XDP_PATH = "/ip/csdn-ip2region.xdb";
    private static Searcher searcher = null;

    public static String getUserAgentKey() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        UserAgent userAgent = getUserAgent(request);
        return userAgent.getOperatingSystem().getName() + userAgent.getBrowser().getName() + userAgent.getBrowserVersion();
    }

    public static String getUserAgentKey(HttpServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        return userAgent.getOperatingSystem().getName() + userAgent.getBrowser().getName() + userAgent.getBrowserVersion();
    }

    /**
     * 获取user-agent
     * @return
     */
    public static UserAgent getUserAgent() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }
    /**
     * 获取user-agent
     * @param request
     * @return
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

    /**
     * 获取ip地址归属地信息
     * @param ip
     * @return
     */
    public static String getIpInfo(String ip) {
        String info = "";
        info = getIpInfoByMemorySearch(ip);
        if ("".equalsIgnoreCase(info) || "未知".equals(info)) {
            info = getIpInfoByOnline(ip);
        }
        return info;
    }


    /**
     * 在线查询IP归属地
     */
    public static String getIpInfoByOnline(String ip) {
        try {
            //1、创建 URLConnction
            URL url = new URL("http://ip-api.com/json/" + ip + "?lang=zh-CN");

            //2、设置connection的属性
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.setRequestProperty("content-type", "application/json; charset=utf-8");

            //3.连接
            connection.connect();

            //4.获取内容
            InputStream inputStream = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            String str = sb.toString();
            if (StringUtils.isNotEmpty(str)) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map map = objectMapper.readValue(str, new TypeReference<LinkedHashMap<String,Object>>() {});
                String country = map.get("country") == null ? "" : (String) map.get("country");
                String city = map.get("city") == null ? "" : (String) map.get("city");
                String regionName = map.get("regionName") == null ? "" : (String) map.get("regionName");
                return String.format("%s|%s|%s|%s|%s", country, "0", regionName, city,"0");
            }
        } catch (Exception e) {
            log.error("在线查询IP地址异常, {}", e.getMessage());
        }
        return "未知";
    }


    /**
     * Ip2region 获取ip地址归属地信息
     * @param ip
     * @return
     */
    public static String getIpInfoByMemorySearch(String ip) {
        try {
            if (!initSearcher()) {
                return "未知";
            }
            String region = searcher.search(ip);
            return region;
        } catch (Exception e) {
            log.info("本地查询ip地址获取异常, {}", e.getMessage());
            return "未知";
        }
    }

    /**
     * 初始化search对象
     */
    public static boolean initSearcher() {
        if (searcher != null) return true;

        try {
            File file = new ClassPathResource(XDP_PATH).getFile();
            FileChannel channel = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer buff = ByteBuffer.allocateDirect((int) channel.size());
            channel.read(buff);
            buff.flip();
            byte[] data = new byte[buff.limit()];
            buff.get(data);
            searcher = Searcher.newWithBuffer(data);
            return true;
        } catch (IOException e) {
            log.error("searcher 初始化失败!");
        }
        return false;
    }

    /**
     * 获取 IP 地址
     */
    public static String getIpAddress() {
        HttpServletRequest request;
        return getIpAddress(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest());
    }
    /**
     * 获取请求的 IP 地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip)) {
                // 根据网卡取本机配置的 IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("获取IP地址异常，{}", e.getMessage());
                }
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        // 本机访问
        if ("localhost".equalsIgnoreCase(ip) || "127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            // 根据网卡取本机配置的IP
            InetAddress inet;
            try {
                inet = InetAddress.getLocalHost();
                ip = inet.getHostAddress();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取本机IP地址异常，{}", e.getMessage());
            }
        }
        // 如果查找不到 IP,可以返回 127.0.0.1，可以做一定的处理，但是这里不考虑
        // if (ip == null) {
        //     return "127.0.0.1";
        // }
        return ip;
    }
//    /**
//     * 获取IP地址
//     */
//    public static String getIpAddress(ServerHttpRequest request) {
//        HttpHeaders headers = request.getHeaders();
//        String ipAddress = headers.getFirst("X-Forwarded-For");
//        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
//            ipAddress = headers.getFirst("Proxy-Client-IP");
//        }
//        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
//            ipAddress = headers.getFirst("WL-Proxy-Client-IP");
//        }
//        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
//            ipAddress = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
//            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
//                // 根据网卡取本机配置的IP
//                try {
//                    InetAddress inet = InetAddress.getLocalHost();
//                    ipAddress = inet.getHostAddress();
//                } catch (Exception e) {
//                    log.error("获取IP地址异常,{}", e.getMessage());
//                }
//            }
//        }
//
//        // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
//        if (ipAddress != null && ipAddress.indexOf(",") > 0) {
//            ipAddress = ipAddress.split(",")[0];
//        }
//        return ipAddress;
//    }

    /**
     * 获取mac地址
     */
    public static String getMacIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] macAddressBytes = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            // 将mac地址拼装成String
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < macAddressBytes.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(macAddressBytes[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            return sb.toString().trim().toUpperCase();
        } catch (Exception e) {
            log.error("Mac获取IP地址异常,{}", e.getMessage());
        }
        return "";
    }

}
