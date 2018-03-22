package com.csi.test.service;

import com.csi.test.domain.Price;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PriceServiceImpl implements PriceService {
    @Override
    public Collection<Price> updatePrices(Collection<Price> oldPrices, Collection<Price> newPrices) {
        Objects.requireNonNull(oldPrices);
        Objects.requireNonNull(newPrices);

        if (newPrices.isEmpty()) {
            return new ArrayList<>(oldPrices);
        } else if (oldPrices.isEmpty()) {
            return new ArrayList<>(newPrices);
        }

        Map<Price, List<Price>> result = Stream.concat(oldPrices.stream(), newPrices.stream())
                .collect(Collectors.toMap(Function.identity(),
                        Arrays::asList, this::mergePrices));

        return result.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Price> mergePrices(List<Price> priceList1, List<Price> priceList2) {
        Price price1 = priceList1.get(0);
        Price price2 = priceList2.get(0);

        Date beginPrice1 = price1.getBegin();
        Date endPrice1 = price1.getEnd();
        Date beginPrice2 = price2.getBegin();
        Date endPrice2 = price2.getEnd();

        if (isDatesOverlaps(beginPrice1, endPrice1, beginPrice2, endPrice2)) {
            if (price1.getValue() == price2.getValue()) {
                price1.setBegin(beginPrice1.before(beginPrice2) ? beginPrice1 : beginPrice2);
                price1.setEnd(endPrice1.after(endPrice2) ? endPrice1 : endPrice2);
                return Collections.singletonList(price1);
            } else {
                if (beginPrice1.before(beginPrice2)) {
                    return intersectPrices(price1, price2);
                } else {
                    return intersectPrices(price2, price1);
                }
            }
        } else {
            return Arrays.asList(price1, price2);
        }
    }

    private boolean isDatesOverlaps(Date beginPrice1, Date endPrice1, Date beginPrice2, Date endPrice2) {
        return beginPrice1.before(endPrice2) && endPrice1.after(beginPrice2);
    }

    private List<Price> intersectPrices(Price price1, Price price2) {
        Date beginPrice2 = price2.getBegin();
        Date endPrice1 = price1.getEnd();
        Date endPrice2 = price2.getEnd();

        if (endPrice2.before(endPrice1)) {
            Price price3 = new Price()
                    .setProduct_code(price1.getProduct_code())
                    .setNumber(price1.getNumber())
                    .setDepart(price1.getDepart())
                    .setBegin(endPrice2)
                    .setEnd(endPrice1)
                    .setValue(price1.getValue());

            price1.setEnd(beginPrice2);
            return Arrays.asList(price1, price2, price3);
        } else {
            price1.setEnd(beginPrice2);
            return Arrays.asList(price1, price2);
        }
    }
}
