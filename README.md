# CSI
Tests for CSI company

## Build
Last status on [Travis CI](https://travis-ci.org/Terentich/csi/builds): [![Build Status](https://travis-ci.org/Terentich/csi.svg?branch=master)](https://travis-ci.org/Terentich/csi)

## Description
Используя язык Java необходимо написать метод объединения имеющихся цен с вновь импортированными из внешней системы. Также необходимо написать unit тесты, для проверки хотя бы некоторых возможных вариантов.

Каждый продаваемый товар, имеет свою цену. Цен у товара может быть несколько, каждая цена имеет свой номер, принадлежность к отделу, период действия и значение в валюте. 

В базе данных для каждого товара хранится история цен. В один момент времени может действовать только одна цена из цен, с одинаковым номером и отделом. Обычно товар продается по первой цене, вторая, третья и четвертая могут используются для применения скидок (т.е. сработало условие скидки, товар будет продан по цене номер 2). Касса может обслуживать какой-то отдел, тогда она будет использовать при продаже цены указанные для этого отдела.

Правила объединения цен:
- если товар еще не имеет цен, или имеющиеся цены не пересекаются в периодах действия с новыми, то новые цены просто добавляются к товару;
- если имеющаяся цена пересекается в периоде действия с новой ценой, то:
    - если значения цен одинаковы, период действия имеющейся цены увеличивается согласно периоду новой цены;
    - если значения цен отличаются, добавляется новая цена, а период действия старой цены уменьшается согласно периоду новой цены.
	
Метод в качестве параметров получает коллекцию имеющихся цен, новых цен и должен вернуть коллекцию объединенных цен, для дальнейшего сохранения в БД. 

	Пример полей класса «цена»:
	long id; // идентификатор в БД
	String product_code; // код товара
	int number; // номер цены
	int depart; // номер отдела
	Date begin; // начало действия
	Date end; // конец действия
	long value; // значение цены в копейках
