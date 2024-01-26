package io.Adrestus.Backend.MemoryBuffer;

import java.util.TreeSet;

public class AddressMemoryInstance {
    private static volatile AddressMemoryInstance instance;
    private IAddressMemoryPoll memory;

    private AddressMemoryInstance() {
        // to prevent instantiating by Reflection call
        if (instance != null) {
            throw new IllegalStateException("Already initialized.");
        }
        memory = new AddressMemoryPoll();
    }

    public static AddressMemoryInstance getInstance() {

        var result = instance;
        if (result == null) {
            synchronized (AddressMemoryInstance.class) {
                result = instance;
                if (result == null) {
                    result = new AddressMemoryInstance();
                    instance = result;
                }
            }
        }
        return result;
    }

    public IAddressMemoryPoll getMemory() {
        return memory;
    }

    public void setMemory(TreeSet<String> treeSet) {
        this.memory.setResources(treeSet);
    }
}
