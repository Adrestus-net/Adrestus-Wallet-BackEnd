package io.Adrestus.Backend.MemoryBuffer;

import io.Adrestus.bloom_filter.core.BloomObject;

import java.util.List;
import java.util.TreeSet;

public interface IAddressMemoryPoll {

    public boolean add(String value);

    public String get(String toSearch);

    public String[] retrieveAll();

    public List<String> contains(BloomObject bloomObject);
    public TreeSet<String> getResources();

    public int size();

    public void setResources(TreeSet<String> resources);


}
