package io.Adrestus.Backend.MemoryBuffer;

import java.util.TreeSet;

public interface IAddressMemoryPoll {

    public boolean add(String value);

    public String get(String toSearch);

    public String[] retrieveAll();

    public TreeSet<String> getResources();

    public int size();

    public void setResources(TreeSet<String> resources);
}
