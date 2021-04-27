package com.dyzs.udppaser;

public class External {
    public static void main(String[] args) {
        UdpLogCombine udpLogCombine = new UdpLogCombine();
        udpLogCombine.writeCallConsumeTime();
        udpLogCombine.writeTokenTimestamp();
    }
}
