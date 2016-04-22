package com.github.vladimirdotk.ipcalclite;

public class IpUtils {

    public static boolean validateIp(String ip) {
        if (ip.contains(".")) {
            String[] ipParts = ip.split("\\.");
            if (ipParts.length == 4)  {
                for (String ipPart: ipParts) {
                    if (!validateIpByte(ipPart)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean validateIpByte(String ipPart) {
        int ipInt;

        try{
            ipInt = Integer.parseInt(ipPart);
        } catch (NumberFormatException e) {
            return false;
        }

        return 0 <= ipInt && ipInt <= 255;
    }

    public static boolean validateMask(String mask) {
        int maskInt;

        try{
            maskInt = Integer.parseInt(mask);
        } catch (NumberFormatException e) {
            return false;
        }

        return 1 <= maskInt && maskInt <= 32;
    }

    public static String calculateNetworkIp(String ip, String mask) {

        mask = convertMask(mask);
        String networkIp="";
        String[] ipParts = ip.split("\\.");
        String[] maskParts = mask.split("\\.");

        for(int i = 0; i < 4; i++){
            int x = Integer.valueOf(ipParts[i]);
            int y = Integer.valueOf(maskParts[i]);
            int z = x & y;
            networkIp += z;
            if (i < 3) {
                networkIp += '.';
            }
        }

        return networkIp;
    }

    public static String calculateTotalIps(String mask) {
        return String.valueOf((long) Math.pow(2, 32 - Integer.valueOf(mask)));
    }

    private static String convertMask(String mask) {
        int cidrMask = Integer.valueOf(mask);
        long bits = 0;
        bits = 0xffffffff ^ (1 << 32 - cidrMask) - 1;
        return String.format(
                "%d.%d.%d.%d",
                (bits & 0x0000000000ff000000L) >> 24,
                (bits & 0x0000000000ff0000) >> 16,
                (bits & 0x0000000000ff00) >> 8,
                bits & 0xff
        );
    }


}

