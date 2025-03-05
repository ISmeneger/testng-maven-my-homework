import calculation_of_the_shipping_cost.CargoDimension;
import calculation_of_the_shipping_cost.Delivery;
import calculation_of_the_shipping_cost.ServiceWorkload;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static calculation_of_the_shipping_cost.CargoDimension.LARGE;
import static calculation_of_the_shipping_cost.CargoDimension.SMALL;
import static calculation_of_the_shipping_cost.ServiceWorkload.*;

public class DeliveryCalculatorTest {
    @Test(groups = "positive", description = "Расчет стоимости минимальной доставки")
    void testMinDeliveryCost() {
        Delivery delivery = new Delivery(1, LARGE, false, INCREASED);
        Assert.assertEquals(delivery.calculateDeliveryCost(), 400);
    }

    @Test(groups = "positive", description = "Проверка расчета стоимости доставки в зависимости от расстояния", dataProvider = "distanceDelivery")
    void testDistanceBasedDeliveryPriceParametrized(int distance, ServiceWorkload serviceWorkload, double expectedDeliveryCost) {
        Delivery delivery = new Delivery(distance, SMALL, false, serviceWorkload);
        Assert.assertEquals(delivery.calculateDeliveryCost(), expectedDeliveryCost);
    }

    @Test(groups = "positive", description = "Проверка расчета стоимости доставки в зависимости от размеров", dataProvider = "sizeBasedDelivery")
    void testSizeBasedDeliveryPriceParametrized(int distance, CargoDimension cargoDimension, double expectedDeliveryCost) {
        Delivery delivery = new Delivery(distance, cargoDimension, false, ServiceWorkload.NORMAL);
        Assert.assertEquals(delivery.calculateDeliveryCost(), expectedDeliveryCost);
    }

    @Test(groups = "positive", description = "Проверка расчета стоимости доставки в зависимости от загруженности",
            dataProvider = "loadBasedDataMethod")
    void testLoadBasedDeliveryPrice(ServiceWorkload deliveryServiceWorkload, int expectedCost) {
        Delivery delivery = new Delivery(15, LARGE, true, deliveryServiceWorkload);
        Assert.assertEquals(expectedCost, delivery.calculateDeliveryCost());
    }

    @Test(groups = "negative", description = "Проверка расчета стоимости доставки на расстояние меньше нуля или 0",
            dataProvider = "negativeDistanceDataMethod",
            expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "destinationDistance should be a positive number!")
    void testNegativeDistanceOrderCost(int distance) {
        Delivery delivery = new Delivery(distance, SMALL, false, ServiceWorkload.NORMAL);
        delivery.calculateDeliveryCost();

    }

    @Test(groups = "negative", description = "Проверка расчета стоимости доставки в зависимости от хрупкости и на расстояние более 30 км")
    void testFragileBasedDeliveryPrice() {
        Delivery delivery = new Delivery(35, SMALL, true, ServiceWorkload.NORMAL);
        Assert.assertThrows(UnsupportedOperationException.class, delivery::calculateDeliveryCost);

    }

    @DataProvider(name = "distanceDelivery")
    public Object[][] distanceBasedDeliveryMethod() {
        return new Object[][] {
                {5, NORMAL, 400},
                {15, HIGH, 420},
                {35, VERY_HIGH, 640}
        };
    }

    @DataProvider(name = "sizeBasedDelivery")
    public Object[][] sizeBasedDeliveryDataMethod() {
        return new Object[][] {
                {5, LARGE, 400},
                {15, SMALL, 400},
                {35, LARGE, 500}
        };
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


