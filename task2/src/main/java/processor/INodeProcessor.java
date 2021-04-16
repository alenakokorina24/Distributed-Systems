package processor;

import model.Node;

public interface INodeProcessor {
    void insertNode(Node node);
    void flush();
}
