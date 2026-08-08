package com.smartlibrary.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete decorator - Featured
 */
public class FeaturedDecorator extends BookDecorator {
    public FeaturedDecorator(IBook wrapped) { super(wrapped); }

    public List<String> getBadges() {
        List<String> base = wrapped instanceof BookDecorator ? ((BookDecorator)wrapped).getBadges() : new ArrayList<>();
        base.add("Featured");
        return base;
    }
}