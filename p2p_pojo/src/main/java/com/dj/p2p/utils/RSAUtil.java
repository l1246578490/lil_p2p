package com.dj.p2p.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;

public class RSAUtil {
	/*私玥
	 * MIICXQIBAAKBgQDlOJu6TyygqxfWT7eLtGDwajtNFOb9I5XRb6khyfD1Yt3YiCgQ
	WMNW649887VGJiGr/L5i2osbl8C9+WJTeucF+S76xFxdU6jE0NQ+Z+zEdhUTooNR
	aY5nZiu5PgDB0ED/ZKBUSLKL7eibMxZtMlUDHjm4gwQco1KRMDSmXSMkDwIDAQAB
	AoGAfY9LpnuWK5Bs50UVep5c93SJdUi82u7yMx4iHFMc/Z2hfenfYEzu+57fI4fv
	xTQ//5DbzRR/XKb8ulNv6+CHyPF31xk7YOBfkGI8qjLoq06V+FyBfDSwL8KbLyeH
	m7KUZnLNQbk8yGLzB3iYKkRHlmUanQGaNMIJziWOkN+N9dECQQD0ONYRNZeuM8zd
	8XJTSdcIX4a3gy3GGCJxOzv16XHxD03GW6UNLmfPwenKu+cdrQeaqEixrCejXdAF
	z/7+BSMpAkEA8EaSOeP5Xr3ZrbiKzi6TGMwHMvC7HdJxaBJbVRfApFrE0/mPwmP5
	rN7QwjrMY+0+AbXcm8mRQyQ1+IGEembsdwJBAN6az8Rv7QnD/YBvi52POIlRSSIM
	V7SwWvSK4WSMnGb1ZBbhgdg57DXaspcwHsFV7hByQ5BvMtIduHcT14ECfcECQATe
	aTgjFnqE/lQ22Rk0eGaYO80cc643BXVGafNfd9fcvwBMnk0iGX0XRsOozVt5Azil
	psLBYuApa66NcVHJpCECQQDTjI2AQhFc1yRnCU/YgDnSpJVm1nASoRUnU8Jfm3Oz
	uku7JUXcVpt08DFSceCEX9unCuMcT72rAQlLpdZir876*/
//	private static String REQUEST_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALuYr8WlpQbDmQ1Q" + "O5Wz0f85+UhIH1GHbIwCXU+LoPIX2bqHkzE3B3ZIMSvbCWT5pErv0lyM+7X77TSU" + "42kUC36UysQEu9MwdlVD6RaBVfT6MYoJpvvGgq+VlM82OU3P03e7iDoppsENchlw" + "lr93x6xm/MuyzzRI1BTz0H6+rU1lAgMBAAECgYAdcXN1A/CIxT5KVqNjdZuqAUFc" + "1OUFeMnSl7RpfbK/DHtByXGSsd5b9Cyzg2dQD9Z3ZHiRyhbfkzDBpfSjU2ASMrR0" + "xHGLdJx7DSLd3k75ifF/DECAQA/CzIfiCKa9IgA2Cj//OVLcxjGAw4iEnE9Umsa7" + "n2wyR8bouDarHwifwQJBAPT4IcVaChm0Q8GEZwtyf/FIS0kEQ+u6r2zHja4Nlz+s" + "rR3EwggtV+Me9v2xyJfpcO/mbVyOCdaav17Wr7yD+tkCQQDECzFh0cjviS6wFw8Z" + "+vSWv4IBtGBVbhVB30LQFBMg96b3Av89pULpKRl2mIEVOtfo5IhLaGWhiB4z4Jar" + "QhdtAkEAlZcAaFc3W8LsrTuBAUiGQHz5HDlykHyLq02gguzhs4xqmocQRZYK2TKL"
//            + "eRgbekifIp//oElMULRmsC9BWUju4QJAL5qwMRqp+lCLf8L5rctcnUZ/oT5Vrij/" + "DHHUXYaiZnz8lDqsFCIPL2MFheDeZ3NUfn8QAY+mLiVJgDtnGsr/uQJAPmlbiqyY" + "oqrN0Th78PnzOiOmJokLSzOKfg9xpyp4Ae5dGk2tyHsaieIdT+otGAnib5suxodr" + "cSTlMXhIikLimg==";

	private static String REQUEST_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDno3UKrMfJ3NhuH25JN835pOeyIyZgo43lEyTdL/q97d+Y56XyIlPl126i81r9zfrGzKTxduRPGU6obnglHUDSBtDa8JmK7hFEOni7FE6qbjTKrEN3futa8aq+0kY6RlKi3g2Tr5A3zg23wENaHDeGAHZAdGvOKU9O/z6fhwewS/f/0ueD/HuxDCR4wlQ/VMMglDnMAG0v+w5jTcBd2w4+L6djLaI+grg6UzPFzcy0wodA20fHLqIT1So9TONldrM3COA7zUxOauklwAU5w6fW4qPUx5USYRDIPDs2qydvw9WMh4Q2wPayv+7ORV5cX9KL5OUG4M0fuDuquOS0XNrtAgMBAAECggEAd9t9eWuT6WDL4JeE4n/spYttlCBaAFFA39J7FbR44wQN2bmrhVG75ccGaWzQnVmStIE5q/PgCQnISKD4rAOqAcMjGNa8LEYpSwuSYwML/WYPDPv74P4cXC+GMTzut8KhicKqdcu3LRkCwhzK/PpyxHTCu3FCQyfe8LpkNfmRwedhKKrW5nKT3wRQqy8dNNLDMOHMcjSSVg2zuVHrZBMIWCY1PFOuFV7xwjdSPWnJuG+ncNPsPCKE7a0s7tymHcyfqg1gUK/1V0X7bcYaqXS9gO3+a2CsiDGH4s9zaj/MrZQCx5LCj6RH4l+bXQf3TWxOy3NdK93D4/Xlwm+LOFJz4QKBgQD7CQWVl5Hd7TaqMIETdBqug6tc9U3/iP4MLkh/617cxNbef4Tz9D9HZMMfI8bZ8cjFBzY647KaEybVdgBH3dM7a1GUgLOJ/7mwVV71zqIPAIGFUjB81HHPDTWz1Yja/6Ipmt5A2bHAKd1CNGTqRQVHe7tsHWQfCq6wYMb0p6c+yQKBgQDsODsQy2xIQEIJgkl6j4NeJvBTiMY32igEl60BDJ72KpUGMxtnCmWwvJy41kRntERaXhjRAhVJWrgcPCQVcCPSsqMoztMVXJSPWhwaULTCymkBY+ElLLbmzkX6WRmcK2YOkcw2/MVbAeMul9A4YcLi4H7NsAITYvgP9Jtfub8ZBQKBgDhy+ed+ktV1rxIsIApQ1GMjxpf7rVNl6cKxcaoaMlWwiaS0xh/Zb5VNSjcpY2DE5uGvUnmBlDrjTZs4kq2WaxZ0dn7PRhAlCgtl1xmtInH/KiDhr3eSihkGHmpW/RRJ4Zw7b/jjOv48K73kbEDlzl8ZqraQEWCNlWSiDnsbbBdJAoGBAMaLDLo1ALJJsDel1nS5I0WDnQPZNiHDr1cM1VDIduOZLuCjHCkjSA2gumlxTtWLYthBtsIV6iZZpd21fu617qYtl425KRoPUp6asnw70XLYBiIw/Q/t4V1litO9CBWHNKSSTv74vjicAYMr/sslAFUJNAtWIKiCytU+V0nCKaixAoGBAOzMnr6QH5bJHvftlv7ArJDGS6Lp7SxzfBSksAi1Kszl2osh3lQokoHAJZh1UN6Bznw0pXJL7HKehRITuSTvMnsOpuwKUGTB/xIXeESv5zQv8p7IoeaU0dVslYfDgvkKDP9ViYsL4MYhLHHVtR8ECaKe7wxPHB6/rKALS+jebkCQ";
    /**
     * 解密算法
     * cryptograph:密文
     */
    public static String decryptRequestParamValue(String cryptograph) throws Exception {
        PrivateKey key = stringToPrivateKey(REQUEST_PRIVATE_KEY);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b1 = decoder.decodeBuffer(cryptograph);
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }
	
    public static PrivateKey stringToPrivateKey(String s) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] c = null;
        KeyFactory keyFact = null;
        PrivateKey returnKey = null;

        try {

            c = decoder.decodeBuffer(s);
            keyFact = KeyFactory.getInstance("RSA");
        } catch (Exception e) {

            System.out.println("Error in first try catch of stringToPrivateKey");
            e.printStackTrace();
        }

        PKCS8EncodedKeySpec x509KeySpec = new PKCS8EncodedKeySpec(c);
        try { // the next line causes the crash
            returnKey = keyFact.generatePrivate(x509KeySpec);
        } catch (Exception e) {

            System.out.println("Error in stringToPrivateKey");
            e.printStackTrace();
        }

        return returnKey;

    }

}
