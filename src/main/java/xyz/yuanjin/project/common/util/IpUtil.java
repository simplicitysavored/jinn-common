package xyz.yuanjin.project.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yuanjin.project.common.util.ipv6.IPv6Address;
import xyz.yuanjin.project.common.util.ipv6.IPv6AddressRange;
import xyz.yuanjin.project.common.util.ipv6.IPv6Network;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author yuanjin
 */
@SuppressWarnings("unused")
public class IpUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);

    private static final int[] CIDR2MASK = new int[]{0x00000000, 0x80000000,
            0xC0000000, 0xE0000000, 0xF0000000, 0xF8000000, 0xFC000000,
            0xFE000000, 0xFF000000, 0xFF800000, 0xFFC00000, 0xFFE00000,
            0xFFF00000, 0xFFF80000, 0xFFFC0000, 0xFFFE0000, 0xFFFF0000,
            0xFFFF8000, 0xFFFFC000, 0xFFFFE000, 0xFFFFF000, 0xFFFFF800,
            0xFFFFFC00, 0xFFFFFE00, 0xFFFFFF00, 0xFFFFFF80, 0xFFFFFFC0,
            0xFFFFFFE0, 0xFFFFFFF0, 0xFFFFFFF8, 0xFFFFFFFC, 0xFFFFFFFE,
            0xFFFFFFFF};

    private static List<String> range2cidrlistV2(String startIp) {
        return fromIpv4AndMask(startIp, 27);
    }

    /**
     * 根据 ip/掩码位 获取开始ip和结束ip
     *
     * @param ip   ip
     * @param mask 掩码位
     * @return 开始ip：pair.get(0)， 结束ip：pair.get(0)；
     */
    private static List<String> fromIpv4AndMask(String ip, Integer mask) {
        ArrayList<String> pairs = new ArrayList<>();
        long ipLong = ipToLong(ip);
        long maskTmp = -1;
        maskTmp = maskTmp << (32 - mask);
        pairs.add(longToIP(ipLong & maskTmp));
        pairs.add(longToIP(ipLong | ~maskTmp));
        return pairs;
    }

    private static List<String> range2cidrlist(String startIp, String endIp) {
        long start = ipToLong(startIp);
        long end = ipToLong(endIp);

        ArrayList<String> pairs = new ArrayList<>();
        while (end >= start) {
            byte maxsize = 27;
            while (maxsize > 0) {
                long mask = CIDR2MASK[maxsize - 1];
                long maskedBase = start & mask;

                if (maskedBase != start) {
                    break;
                }
                maxsize--;
            }
            double x = Math.log(end - start + 1) / Math.log(2);
            byte maxdiff = (byte) (32 - Math.floor(x));
            if (maxsize < maxdiff) {
                maxsize = maxdiff;
            }
            String ip = longToIP(start);
            pairs.add(ip + "/" + maxsize);
            start += Math.pow(2, (32 - maxsize));
        }
        return pairs;
    }

    /**
     * 仅适用 ipv4
     *
     * @param strIP
     * @return
     */
    public static long ipToLong(String strIP) {
        long[] ip = new long[4];
        String[] ipSec = strIP.split("\\.");
        for (int k = 0; k < 4; k++) {
            ip[k] = Long.valueOf(ipSec[k]);
        }
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    private static byte[] ipv4ToBytes(String ipv4) {
        byte[] ret = new byte[5];
        ret[0] = 0;
        // 先找到IP地址字符串中.的位置
        int position1 = ipv4.indexOf(".");
        int position2 = ipv4.indexOf(".", position1 + 1);
        int position3 = ipv4.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ret[1] = (byte) Integer.parseInt(ipv4.substring(0, position1));
        ret[2] = (byte) Integer.parseInt(ipv4.substring(position1 + 1,
                position2));
        ret[3] = (byte) Integer.parseInt(ipv4.substring(position2 + 1,
                position3));
        ret[4] = (byte) Integer.parseInt(ipv4.substring(position3 + 1));
        return ret;
    }

    private static byte[] ipv6ToBytes(String ipv6) {
        byte[] ret = new byte[17];
        ret[0] = 0;
        int ib = 16;
        // ipv4混合模式标记
        boolean comFlag = false;
        // 去掉开头的冒号
        if (ipv6.startsWith(":")) {
            ipv6 = ipv6.substring(1);
        }
        String[] groups = ipv6.split(":");
        // 反向扫描
        for (int ig = groups.length - 1; ig > -1; ig--) {
            if (groups[ig].contains(".")) {
                // 出现ipv4混合模式
                byte[] temp = ipv4ToBytes(groups[ig]);
                ret[ib--] = temp[4];
                ret[ib--] = temp[3];
                ret[ib--] = temp[2];
                ret[ib--] = temp[1];
                comFlag = true;
            } else if ("".equals(groups[ig])) {
                // 出现零长度压缩,计算缺少的组数
                int zlg = 9 - (groups.length + (comFlag ? 1 : 0));
                // 将这些组置0
                while (zlg-- > 0) {
                    ret[ib--] = 0;
                    ret[ib--] = 0;
                }
            } else {
                int temp = Integer.parseInt(groups[ig], 16);
                ret[ib--] = (byte) temp;
                ret[ib--] = (byte) (temp >> 8);
            }
        }
        return ret;
    }

    /**
     * 适用 ipv4 和 ipv6
     *
     * @param ip
     * @return
     */
    public static Long getIPValue(String ip) {
        ip = ip.replace(" ", "");
        byte[] bytes;
        if (ip.contains(":")) {
            bytes = ipv6ToBytes(ip);
        } else {
            bytes = ipv4ToBytes(ip);
        }
        BigInteger bigInt = new BigInteger(bytes);
        return bigInt.longValue();
    }


    public static String longToIP(long value) {
        return "" + (value >>> 24) +
                "." +
                ((value & 0x00FFFFFF) >>> 16) +
                "." +
                ((value & 0x0000FFFF) >>> 8) +
                "." +
                (value & 0x000000FF);
    }

    public static String intToMask(int bitMask) {
        if (bitMask > 32) {
            return null;
        }

        int[] tmpMask = {0, 0, 0, 0};
        int times = bitMask / 8;
        int i = 0;
        for (; i < times; i++) {
            tmpMask[i] = 255;
        }

        for (int j = 1; j <= 8; j++) {
            if (j <= (bitMask - times * 8)) {
                tmpMask[i - 1] = 2 * tmpMask[i - 1] + 1;
            } else {
                tmpMask[i - 1] = 2 * tmpMask[i - 1];
            }
        }
        return Integer.toString(tmpMask[0]) + "." + Integer.toString(tmpMask[1]) + "." + Integer.toString(tmpMask[2]) + "." + Integer.toString(tmpMask[3]);
    }

    /**
     * 通过开始ip和结束ip获取 ip/掩码位
     *
     * @param ipStart 开始ip
     * @param ipEnd   结束ip
     * @return 多个 ip/掩码位 格式的字符串
     */
    private static List<String> toIpv4AndMask(String ipStart, String ipEnd) {
        List<String> retList = new ArrayList<>();

        List<Integer> masks = new ArrayList<>();
        masks.add(32);
        masks.add(31);
        masks.add(30);
        masks.add(29);
        masks.add(28);

        boolean flag = false;
        long ipstart = ipToLong(ipStart);
        long ipend = ipToLong(ipEnd);

        String ipStartString = "";
        String ipEndString = "";
        String maskV2 = "";

        for (Integer temp : masks) {
            long maskLong = -1;
            maskLong = maskLong << 32 - temp;
            String ipstartMask = longToIP(ipstart & maskLong);
            String ipendMask = longToIP(ipend & maskLong);
            if (ipstartMask.trim().equals(ipendMask.trim())) {
                flag = true;
                ipStartString = ipstartMask;
                ipEndString = ipendMask;
                maskV2 = String.valueOf(temp);
                break;
            }
        }

        if (flag) {
            String ipAndMask = ipStartString + "/" + maskV2;
            retList.add(ipAndMask);
            return retList;
        } else {
            List<String> ipsBigStart = range2cidrlistV2(ipStart);
            List<String> ipsBigEnd = range2cidrlistV2(ipEnd);

            retList = range2cidrlist(ipsBigStart.get(0), ipsBigEnd.get(1));

            return retList;
        }

    }


    public static List<String> toIpAndMask(String ipStart, String ipEnd) {
        int ipType1 = isIpv4OrIpv6(ipStart);
        int ipType2 = isIpv4OrIpv6(ipEnd);

        List<String> ipAndMaskList = new ArrayList<>();
        if (ipType1 != ipType2) {
            return ipAndMaskList;
        }

        if (ipType1 == 4) {
            ipAndMaskList = toIpv4AndMask(ipStart, ipEnd);
        } else if (ipType1 == 6) {
            final IPv6AddressRange range = IPv6AddressRange.fromFirstAndLast(IPv6Address.fromString(ipStart),
                    IPv6Address.fromString(ipEnd));

            Iterator<IPv6Network> subnetsIterator = range.toSubnets();
            while (subnetsIterator.hasNext()) {
                ipAndMaskList.add(subnetsIterator.next().toString());
            }
        }
        // 检查ipv6网段格式是否每段4位字符, 缺少则补0
        checkIpiv6Range(ipAndMaskList);
        return ipAndMaskList;
    }

    /**
     * 检查ipv6是否符合规范, 主要是高位需要补充0
     */
    private static void checkIpiv6Range(List<String> ranges) {
        for (int m = 0; m < ranges.size(); m++) {
            if (ranges.get(m).contains(":")) {
                // 有 / 的表示有网络位
                boolean isRange = false;
                String[] arr = ranges.get(m).split(":");
                for (int i = 0; i < arr.length; i++) {
                    if (!(isRange = arr[i].contains("/"))) {
                        switch (arr[i].length()) {
                            case 1:
                                arr[i] = "000" + arr[i];
                                break;
                            case 2:
                                arr[i] = "00" + arr[i];
                                break;
                            case 3:
                                arr[i] = "0" + arr[i];
                                break;
                            default:
                                break;
                        }
                    }
                }
                String str = CollectionUtil.listToString(Arrays.asList(arr), ":");
                if (!str.contains("::") && str.split(":").length < 8) {
                    if (str.endsWith(":")) {
                        str += ":";
                    } else {
                        str += "::";
                    }
                }
                ranges.set(m, str);
            }
        }
    }

    public static List<String> fromIpAndMask(String ip, Integer mask) {
        int ipType = isIpv4OrIpv6(ip);

        List<String> ipStartAndEtartList = new ArrayList<>();
        if (ipType == 4) {
            ipStartAndEtartList = fromIpv4AndMask(ip, mask);
        } else if (ipType == 6) {
            final IPv6Network strangeNetwork = IPv6Network.fromString(ip + "/" + mask.toString());
            ipStartAndEtartList.add(strangeNetwork.getFirst().toString());
            ipStartAndEtartList.add(strangeNetwork.getLast().toString());
        }

        checkIpiv6Range(ipStartAndEtartList);
        return ipStartAndEtartList;
    }

    public static int isIpv4OrIpv6(String ipAdress) {
        try {
            InetAddress address = InetAddress.getByName(ipAdress);
            if (address instanceof Inet4Address) {
                return 4;
            } else if (address instanceof Inet6Address) {
                return 6;
            }
        } catch (UnknownHostException e) {
            LOGGER.info("判断ip类型异常: " + ipAdress);
        }
        return 0;
    }

    /***
     * 匹配ipv6是否在开始和结束ip内
     * @param ipv6Start 开始ip
     * @param ipv6End 结束ip
     * @param ipv6Sour 需要匹配的ipv6
     * @return
     */
    public static boolean matchIpv6AddressInRange(String ipv6Start, String ipv6End, String ipv6Sour) {

        try {
            IPv6AddressRange iPv6AddressRange = IPv6Network.fromFirstAndLast(
                    IPv6Address.fromString(ipv6Start),
                    IPv6Address.fromString(ipv6End)
            );
            return iPv6AddressRange.contains(IPv6Address.fromString(ipv6Sour));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return false;
    }

}
