package io.Adrestus.Backend.MemoryBuffer;

import io.Adrestus.bloom_filter.BloomFilter;
import io.Adrestus.bloom_filter.core.BloomObject;
import io.Adrestus.bloom_filter.impl.InMemoryBloomFilter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class AddressMemoryPoll implements IAddressMemoryPoll {
    private TreeSet<String> resources;

    public AddressMemoryPoll() {
        resources = new TreeSet<>();
    }

    @Override
    public boolean add(String value) {
        return resources.add(value);
    }

    @Override
    public String get(String toSearch) {
        return resources.stream().filter(x -> x.equals(toSearch)).findFirst().get();
    }

    @Override
    public String[] retrieveAll() {
        String arr[] = new String[resources.size()];
        return resources.toArray(arr);
    }

    @Override
    public List<String> contains(BloomObject bloomObject) {
        BloomFilter<String> match_filter = new InMemoryBloomFilter<String>(bloomObject.getNumBitsRequired(), bloomObject.getHashFunctionNum(), bloomObject.getArray(), null);
        List<String> buffer = new ArrayList<String>(AddressMemoryInstance.getInstance().getMemory().getResources());
        List<Integer>toDelete=new ArrayList<>();

        for (int i = 0; i < buffer.size(); i++) {
            if (!match_filter.contains(buffer.get(i))) {
                toDelete.add(i);
            }
        }
        toDelete.sort(Comparator.reverseOrder());
        toDelete.stream().mapToInt(i -> i).forEach(buffer::remove);
        return buffer;
    }

    @Override
    public TreeSet<String> getResources() {
        return resources;
    }

    @Override
    public int size() {
        return resources.size();
    }

    @Override
    public void setResources(TreeSet<String> resources) {
        this.resources = resources;
    }


}
