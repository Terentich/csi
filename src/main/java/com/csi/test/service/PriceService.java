package com.csi.test.service;

import com.csi.test.domain.Price;

import java.util.Collection;

@FunctionalInterface
public interface PriceService {
    /**
     * Update prices and return new collection.
     *
     * @param oldPrices old prices collection
     * @param newPrices new prices collection
     * @return new collection with updated prices (or empty list)
     * @throws NullPointerException if {@code oldPrices} or {@code newPrices} is null
     */
    Collection<Price> updatePrices(Collection<Price> oldPrices, Collection<Price> newPrices);
}
