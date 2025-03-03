import calculation_of_the_shipping_cost.CargoDimension;
import calculation_of_the_shipping_cost.Delivery;
import calculation_of_the_shipping_cost.ServiceWorkload;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static calculation_of_the_shipping_cost.ServiceWorkload.*;

public class DeliveryCalculatorTest {
    @Test(groups = "positive", description = "Расчет стоимости минимальной доставки")
    void testMinDeliveryCost() {
        Delivery delivery = new Delivery(1, CargoDimension.LARGE, false, INCREASED);
        Assert.assertEquals(delivery.calculateDeliveryCost(), 400);
    }

    @Test(groups = "positive", description = "Проверка расчета стоимости доставки в зависимости от расстояния")
    void testDistanceBasedDeliveryPrice() {
        Delivery delivery_1 = new Delivery(5, CargoDimension.SMALL, false, ServiceWorkload.NORMAL);
        Assert.assertEquals(delivery_1.calculateDeliveryCost(), 400);
        Delivery delivery_2 = new Delivery(15, CargoDimension.SMALL, false, HIGH);
        Assert.assertEquals(delivery_2.calculateDeliveryCost(), 420);
        Delivery delivery_3 = new Delivery(35, CargoDimension.SMALL, false, VERY_HIGH);
        Assert.assertEquals(delivery_3.calculateDeliveryCost(), 640);
    }

    @Test(groups = "positive", description = "Проверка расчета стоимости доставки в зависимости от размеров")
    void testSizeBasedDeliveryPrice() {
        Delivery delivery_1 = new Delivery(5, CargoDimension.LARGE, false, ServiceWorkload.NORMAL);
        Assert.assertEquals(delivery_1.calculateDeliveryCost(), 400);
        Delivery delivery_2 = new Delivery(15, CargoDimension.LARGE, false, INCREASED);
        Assert.assertEquals(delivery_2.calculateDeliveryCost(), 480);
        Delivery delivery_3 = new Delivery(35, CargoDimension.LARGE, false, VERY_HIGH);
        Assert.assertEquals(delivery_3.calculateDeliveryCost(), 800);
    }

    @Test(groups = "positive", description = "Проверка расчета стоимости доставки в зависимости от загруженности",
            dataProvider = "loadBasedDataMethod")
    void testLoadBasedDeliveryPrice(ServiceWorkload deliveryServiceWorkload, int expectedCost) {
        Delivery delivery = new Delivery(15, CargoDimension.LARGE, true, deliveryServiceWorkload);
        Assert.assertEquals(expectedCost, delivery.calculateDeliveryCost());
    }

    @Test(groups = "negative", description = "Проверка расчета стоимости доставки на расстояние меньше нуля или 0",
            dataProvider = "negativeDistanceDataMethod",
            expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "destinationDistance should be a positive number!")
    void testNegativeDistanceOrderCost(int distance) {
        Delivery delivery = new Delivery(-1, CargoDimension.SMALL, false, ServiceWorkload.NORMAL);
        delivery.calculateDeliveryCost();

    }

    @Test(groups = "negative", description = "Проверка расчета стоимости доставки в зависимости от хрупкости и на расстояние более 30 км",
            expectedExceptions = UnsupportedOperationException.class,
            expectedExceptionsMessageRegExp = "Fragile cargo cannot be delivered for the distance more than 30")
    void testFragileBasedDeliveryPrice() {
        Delivery delivery = new Delivery(35, CargoDimension.SMALL, true, ServiceWorkload.NORMAL);
        delivery.calculateDeliveryCost();
    }

    @DataProvider
    public Object[][] loadBasedDataMethod() {
        return new Object[][]{
                {VERY_HIGH, 1120},
                {HIGH, 980},
                {INCREASED, 840},
                {NORMAL, 700}
        };
    }

    @DataProvider
    public Object[][] negativeDistanceDataMethod() {
        return new Object[][]{{-1}, {0}};
    }
}


