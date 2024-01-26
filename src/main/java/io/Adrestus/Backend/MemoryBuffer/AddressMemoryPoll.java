package io.Adrestus.Backend.MemoryBuffer;

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
