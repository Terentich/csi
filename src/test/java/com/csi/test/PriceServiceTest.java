package com.csi.test;

import com.csi.test.domain.Price;
import com.csi.test.service.PriceService;
import com.csi.test.service.PriceServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.util.*;

import static org.junit.Assert.*;

public class PriceServiceTest {
    private PriceService priceService;

    @Before
    public void setUp() {
        priceService = new PriceServiceImpl();
    }

    @Test(expected = NullPointerException.class)
    public void testNullOldPricesParameter() {
        priceService.updatePrices(null, Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void testNullNewPricesParameter() {
        priceService.updatePrices(Collections.emptyList(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullParameters() {
        priceService.updatePrices(null, null);
    }

    @Test
    public void testEmptyOldPricesParameter() {
        List<Price> newPrices = new ArrayList<>();
        Price price = new Price(22, "11122312", 1551, 46, getDateOf2013(Month.AUGUST, 26), getDateOf2013(Month.SEPTEMBER, 1), 224_00);
        newPrices.add(price);
        Collection<Price> updatedPrices = priceService.updatePrices(Collections.emptyList(), newPrices);
        assertEquals("Result should be equal to newPrices", newPrices, updatedPrices);
    }

    @Test
    public void testEmptyNewPricesParameter() {
        List<Price> oldPrices = new ArrayList<>();
        Price price = new Price(22, "11122312", 1551, 46, getDateOf2013(Month.APRIL, 5), getDateOf2013(Month.JULY, 20), 1_254_00);
        oldPrices.add(price);

        Collection<Price> updatedPrices = priceService.updatePrices(oldPrices, Collections.emptyList());
        assertEquals("Result should be equal to oldPrices", oldPrices, updatedPrices);
    }

    @Test
    public void testEmptyParameters() {
        Collection<Price> updatedPrices = priceService.updatePrices(Collections.emptyList(), Collections.emptyList());
        assertNotNull("Result should not be null", updatedPrices);
        assertTrue("Result should be empty", updatedPrices.isEmpty());
    }

    @Test
    public void testCorrectParameters() {
        Price price1 = new Price(1, "122856", 1, 1, getJanuary2013(1, false), getJanuary2013(31, true), 11_000);
        Price price2 = new Price(3, "122856", 2, 1, getJanuary2013(10, false), getJanuary2013(20, true), 99_000);
        Price price3 = new Price(5, "6654", 1, 2, getJanuary2013(1, false), getJanuary2013(31, false), 5_000);
        List<Price> oldPrices = Arrays.asList(price1, price2, price3);

        Price price6 = new Price(2, "6654", 1, 2, getJanuary2013(12, false), getJanuary2013(13, false), 4_000);
        Price price5 = new Price(4, "122856", 2, 1, getJanuary2013(15, false), getJanuary2013(25, true), 92_000);
        Price price4 = new Price(6, "122856", 1, 1, getJanuary2013(20, false), getDateOf2013(Month.FEBRUARY, 20), 11_000);
        List<Price> newPrices = Arrays.asList(price4, price5, price6);

        Collection<Price> updatedPrices = priceService.updatePrices(oldPrices, newPrices);
        assertTrue("Result should equals 6", updatedPrices.size() == 6);
        assertTrue(updatedPrices.stream().anyMatch(p -> p.equals(price1)
                && p.getBegin().equals(getJanuary2013(1, false))
                && p.getEnd().equals(getDateOf2013(Month.FEBRUARY, 20))
                && p.getValue() == 11_000));
        assertTrue(updatedPrices.stream().anyMatch(p -> p.equals(price2)
                && p.getBegin().equals(getJanuary2013(10, false))
                && p.getEnd().equals(getJanuary2013(15, false))
                && p.getValue() == 99_000));
        assertTrue(updatedPrices.stream().anyMatch(p -> p.equals(price2)
                && p.getBegin().equals(getJanuary2013(15, false))
                && p.getEnd().equals(getJanuary2013(25, true))
                && p.getValue() == 92_000));
        assertTrue(updatedPrices.stream().anyMatch(p -> p.equals(price3)
                && p.getBegin().equals(getJanuary2013(1, false))
                && p.getEnd().equals(getJanuary2013(12, false))
                && p.getValue() == 5_000));
        assertTrue(updatedPrices.stream().anyMatch(p -> p.equals(price3)
                && p.getBegin().equals(getJanuary2013(12, false))
                && p.getEnd().equals(getJanuary2013(13, false))
                && p.getValue() == 4_000));
        assertTrue(updatedPrices.stream().anyMatch(p -> p.equals(price3)
                && p.getBegin().equals(getJanuary2013(13, false))
                && p.getEnd().equals(getJanuary2013(31, false))
                && p.getValue() == 5_000));
    }

    private Date getJanuary2013(int day, boolean isEndOfDay) {
        LocalDate date = LocalDate.of(2013, Month.JANUARY, day);
        LocalDateTime dateTime = isEndOfDay ? date.atTime(LocalTime.MAX) : date.atStartOfDay();
        return Date.from(dateTime.toInstant(ZoneOffset.UTC));
    }

    private Date getDateOf2013(Month month, int day) {
        LocalDate date = LocalDate.of(2013, month, day);
        return Date.from(date.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
    }
}
