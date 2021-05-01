package ru.nsu.kokorina.distributedsystems.processor;

import ru.nsu.kokorina.distributedsystems.generated.Node;

public interface INodeProcessor {
    void insertNode(Node node);
    void flush();
}
