package com.bytedance.opeditor.ref.service.version;

/**
 * @author fordring
 * @since 2022/8/9
 */
public interface VersionControl {
    long incrAndGetGlobalVersion();

    boolean compareAndSetVersion(String redId, long version);
}
