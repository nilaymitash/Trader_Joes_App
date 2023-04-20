package com.trader.joes.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProductFilter {
    private static Set<String> filterOptions;

    public Set<String> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(Set<String> options) {
        filterOptions = options;
    }
}
