package com.uyu.device.devicetraining.domain.bluetooth;

import timber.log.Timber;

/**
 * Created by windern on 2016/4/27.
 */
public class ComConfig {
    //---.h定义
    public final static byte TRUE = 1;
    public final static byte FALSE = 0;
    public final static int COMBUFLEN = 128;
    public final static int SUBCMDLOOPLENMAX = 6;

    public final static byte COMHead = (byte)0xA5;
    public final static int COMLengthMin = 4;
    public final static int COMLengthMax = COMBUFLEN-6;
    public final static char COMAddr_A = 'A';  //android
    public final static char COMAddr_H = 'H';  //Host STM32
    public final static char COMAddr_P = 'P';  //PC
    public final static char COMAddr_T = 'T';  //测试板

    public final static byte OperationStrByteLength = 4;

    public final static byte MAINCMD_UpgradeStart = 0x00;
    public final static byte MAINCMD_UpgradeData = 0x01;
    public final static byte MAINCMD_UpgradeEnd = 0x02;
    public final static byte MAINCMD_COMMON = 0x10;
    public final static byte MAINCMD_Version = 0x11;

    //Read Input IO
    public final static byte SUBCMD_LimitKey1_LCD = 0x00;
    public final static byte SUBCMD_LimitKey2_DangBan = 0x01;
    public final static byte SUBCMD_LimitKey3_LCD_DangBan = 0x02;
    public final static byte SUBCMD_LimitKey4_TongJu1 = 0x03;
    public final static byte SUBCMD_LimitKey4_TongJu2 = 0x04;
    public final static byte SUBCMD_LightSensor0_R = 0x05;
    public final static byte SUBCMD_LightSensor1_R = 0x06;
    public final static byte SUBCMD_LightSensor2_L = 0x07;
    public final static byte SUBCMD_LightSensor3_L = 0x08;
    public final static byte SUBCMD_LightSensor4_TongJu = 0x09;
    public final static byte SUBCMD_UserBigButton = 0x0a;
    public final static byte SUBCMD_USBChargeCtrl = 0x0b;

    //Read Output IO
    public final static byte SUBCMD_Power12VCtr = 0x20;
    public final static byte SUBCMD_Power5VCtr = 0x21;
    public final static byte SUBCMD_BeepCtr = 0x22;
    public final static byte SUBCMD_DebugLEDCtr = 0x23;
    public final static byte SUBCMD_COM485TRCtrl = 0x24;

    //Motor1
    public final static byte SUBCMD_Motor1_LCD_Reset = 0x30;
    public final static byte SUBCMD_Motor1_LCD_ToPlace = 0x31;
    public final static byte SUBCMD_Motor1_LCD_Place = 0x32;

    //Motor2
    public final static byte SUBCMD_Motor2_DangBan_Reset = 0x40;
    public final static byte SUBCMD_Motor2_DangBan_ToPlace = 0x41;
    public final static byte SUBCMD_Motor2_DangBan_Place = 0x42;
    public final static byte SUBCMD_Motor2_DangBan_Move = 0x43;
    public final static byte SUBCMD_Motor2_DangBan_Stop = 0x44;

    //Motor3
    public final static byte SUBCMD_Motor3_TongJu_Reset = 0x50;
    public final static byte SUBCMD_Motor3_TongJu_SetDistance = 0x51;
    public final static byte SUBCMD_Motor3_TongJu_Distance = 0x52;

    //Motor4
    public final static byte SUBCMD_WoLunMotor4_Reset = 0x60;
    public final static byte SUBCMD_WoLunMotor4_ToLens = 0x61;
    public final static byte SUBCMD_WoLunMotor4_Lens = 0x62;

    //Motor5
    public final static byte SUBCMD_WoLunMotor5_Reset = 0x70;
    public final static byte SUBCMD_WoLunMotor5_ToLens = 0x71;
    public final static byte SUBCMD_WoLunMotor5_Lens = 0x72;

    public final static int initialcrc = 0xFFFFFFFF;
    public final static int[] crc_table = new int[]{
            0x00000000,0x690CE0EE,0x34867077,0x5D8A9099,0x9823B6E0,0xF12F560E,0xACA5C697,0xC5A92679,
            0x4C11DB70,0x251D3B9E,0x7897AB07,0x119B4BE9,0xD4326D90,0xBD3E8D7E,0xE0B41DE7,0x89B8FD09,
            0x2608EDB8,0x4F040D56,0x128E9DCF,0x7B827D21,0xBE2B5B58,0xD727BBB6,0x8AAD2B2F,0xE3A1CBC1,
            0x6A1936C8,0x0315D626,0x5E9F46BF,0x3793A651,0xF23A8028,0x9B3660C6,0xC6BCF05F,0xAFB010B1,
            0x130476DC,0x7A089632,0x278206AB,0x4E8EE645,0x8B27C03C,0xE22B20D2,0xBFA1B04B,0xD6AD50A5,
            0x5F15ADAC,0x36194D42,0x6B93DDDB,0x029F3D35,0xC7361B4C,0xAE3AFBA2,0xF3B06B3B,0x9ABC8BD5,
            0x350C9B64,0x5C007B8A,0x018AEB13,0x68860BFD,0xAD2F2D84,0xC423CD6A,0x99A95DF3,0xF0A5BD1D,
            0x791D4014,0x1011A0FA,0x4D9B3063,0x2497D08D,0xE13EF6F4,0x8832161A,0xD5B88683,0xBCB4666D,
            0x09823B6E,0x608EDB80,0x3D044B19,0x5408ABF7,0x91A18D8E,0xF8AD6D60,0xA527FDF9,0xCC2B1D17,
            0x4593E01E,0x2C9F00F0,0x71159069,0x18197087,0xDDB056FE,0xB4BCB610,0xE9362689,0x803AC667,
            0x2F8AD6D6,0x46863638,0x1B0CA6A1,0x7200464F,0xB7A96036,0xDEA580D8,0x832F1041,0xEA23F0AF,
            0x639B0DA6,0x0A97ED48,0x571D7DD1,0x3E119D3F,0xFBB8BB46,0x92B45BA8,0xCF3ECB31,0xA6322BDF,
            0x1A864DB2,0x738AAD5C,0x2E003DC5,0x470CDD2B,0x82A5FB52,0xEBA91BBC,0xB6238B25,0xDF2F6BCB,
            0x569796C2,0x3F9B762C,0x6211E6B5,0x0B1D065B,0xCEB42022,0xA7B8C0CC,0xFA325055,0x933EB0BB,
            0x3C8EA00A,0x558240E4,0x0808D07D,0x61043093,0xA4AD16EA,0xCDA1F604,0x902B669D,0xF9278673,
            0x709F7B7A,0x19939B94,0x44190B0D,0x2D15EBE3,0xE8BCCD9A,0x81B02D74,0xDC3ABDED,0xB5365D03,
            0x04C11DB7,0x6DCDFD59,0x30476DC0,0x594B8D2E,0x9CE2AB57,0xF5EE4BB9,0xA864DB20,0xC1683BCE,
            0x48D0C6C7,0x21DC2629,0x7C56B6B0,0x155A565E,0xD0F37027,0xB9FF90C9,0xE4750050,0x8D79E0BE,
            0x22C9F00F,0x4BC510E1,0x164F8078,0x7F436096,0xBAEA46EF,0xD3E6A601,0x8E6C3698,0xE760D676,
            0x6ED82B7F,0x07D4CB91,0x5A5E5B08,0x3352BBE6,0xF6FB9D9F,0x9FF77D71,0xC27DEDE8,0xAB710D06,
            0x17C56B6B,0x7EC98B85,0x23431B1C,0x4A4FFBF2,0x8FE6DD8B,0xE6EA3D65,0xBB60ADFC,0xD26C4D12,
            0x5BD4B01B,0x32D850F5,0x6F52C06C,0x065E2082,0xC3F706FB,0xAAFBE615,0xF771768C,0x9E7D9662,
            0x31CD86D3,0x58C1663D,0x054BF6A4,0x6C47164A,0xA9EE3033,0xC0E2D0DD,0x9D684044,0xF464A0AA,
            0x7DDC5DA3,0x14D0BD4D,0x495A2DD4,0x2056CD3A,0xE5FFEB43,0x8CF30BAD,0xD1799B34,0xB8757BDA,
            0x0D4326D9,0x644FC637,0x39C556AE,0x50C9B640,0x95609039,0xFC6C70D7,0xA1E6E04E,0xC8EA00A0,
            0x4152FDA9,0x285E1D47,0x75D48DDE,0x1CD86D30,0xD9714B49,0xB07DABA7,0xEDF73B3E,0x84FBDBD0,
            0x2B4BCB61,0x42472B8F,0x1FCDBB16,0x76C15BF8,0xB3687D81,0xDA649D6F,0x87EE0DF6,0xEEE2ED18,
            0x675A1011,0x0E56F0FF,0x53DC6066,0x3AD08088,0xFF79A6F1,0x9675461F,0xCBFFD686,0xA2F33668,
            0x1E475005,0x774BB0EB,0x2AC12072,0x43CDC09C,0x8664E6E5,0xEF68060B,0xB2E29692,0xDBEE767C,
            0x52568B75,0x3B5A6B9B,0x66D0FB02,0x0FDC1BEC,0xCA753D95,0xA379DD7B,0xFEF34DE2,0x97FFAD0C,
            0x384FBDBD,0x51435D53,0x0CC9CDCA,0x65C52D24,0xA06C0B5D,0xC960EBB3,0x94EA7B2A,0xFDE69BC4,
            0x745E66CD,0x1D528623,0x40D816BA,0x29D4F654,0xEC7DD02D,0x857130C3,0xD8FBA05A,0xB1F740B4
    };


    /**
     * byte 与 int 的相互转换
     * @param x
     * @return
     */
    public static byte intToByte(int x) {
        return (byte) x;
    }

    /**
     * byte 与 int 的相互转换
     * @param b
     * @return
     */
    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

    /**
     * int占字节数组
     * @param res
     * @return
     */
    public static byte[] intToByteArray(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    /**
     * 字节数组转int
     * @param res
     * @return
     */
    public static int byteArrayToInt(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    /**
     * crc32校验
     * @param buffer
     * @param size
     * @return
     */
    public static int calcCrc32(byte[] buffer, int size)
    {
        int i, crc;

        crc = ComConfig.initialcrc;
        for (i = 0; i < size; i++) {
            crc = ComConfig.crc_table[((crc>>24) ^ buffer[i]) & 0xff] ^ (crc << 8);
        }
        return crc ;
    }

    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 字节转换成字符
     * @param bytes
     * @return
     */
    public static String bytesToString(byte[] bytes){
        final StringBuilder stringBuilder = new StringBuilder(bytes.length);
        for(byte byteChar : bytes)
            stringBuilder.append(String.format("%02X ", byteChar));
        return stringBuilder.toString();
    }
}
