package com.hydrovolt.model;

public record TelemetryPacket(
    int deviceId,
    long timestamp,
    float metricValue,
    long crc32
) {}
