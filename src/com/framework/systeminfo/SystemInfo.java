package com.framework.systeminfo;

/**
 * @author zhangkeyi
 *
 * 读取当前java虚拟机以及操作系统的信息，反映当前系统状态
 * 
 */
public class SystemInfo {

    private String vmName;
    private String vmVendor;
    private String vmVersion;
    private String runtimeName;
    private String runtimeVersion;
    private String osName;
    private String osVersion;
    private String cpu;

    long totalMemory    = 0;
    long freeMemory     = 0;
    long totalMemoryKB  = 0;
    long freeMemoryKB   = 0;

    public SystemInfo() {
        vmName          = getProperty("java.vm.name");
        vmVendor        = getProperty("java.vm.vendor");
        vmVersion       = getProperty("java.vm.version");
        runtimeName     = getProperty("java.runtime.name");
        runtimeVersion  = getProperty("java.runtime.version");
        osName          = getProperty("os.name");
        osVersion       = getProperty("os.version");
        cpu             = getProperty("sun.cpu.isalist");

        Runtime runtime = Runtime.getRuntime();
        totalMemory     = runtime.totalMemory();
        freeMemory      = runtime.freeMemory();
        totalMemoryKB   = totalMemory/1024;
        freeMemoryKB    = freeMemory/1024;
    }

    public static String getProperty(String key) {
        String retValue = null;
        try {
            retValue = System.getProperty(key, "");
        } catch (Exception ex) {
            retValue = "no access";
        }
        return retValue;
    }

    public String getCpu() {
        return cpu;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getRuntimeName() {
        return runtimeName;
    }

    public String getRuntimeVersion() {
        return runtimeVersion;
    }

    public String getVmName() {
        return vmName;
    }

    public String getVmVendor() {
        return vmVendor;
    }

    public String getVmVersion() {
        return vmVersion;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public long getFreeMemoryKB() {
        return freeMemoryKB;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public long getTotalMemoryKB() {
        return totalMemoryKB;
    }
}
