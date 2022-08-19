package org.example.strategy;

public interface DataReadWriteSource<ReadType> {
    ReadType getContent();
}
