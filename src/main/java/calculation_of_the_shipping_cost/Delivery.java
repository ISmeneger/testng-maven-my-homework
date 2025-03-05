package calculation_of_the_shipping_cost;

import java.text.DecimalFormat;

public class Delivery {
    public static final double MINIMUM_DELIVERY_AMOUNT = 400;

    private final int destinationDistance;
    private final CargoDimension cargoDimensions;
    private final boolean isFragile;
    private final ServiceWorkload deliveryServiceWorkload;

    public Delivery(int destinationDistance, CargoDimension cargoDimensions, boolean isFragile, ServiceWorkload deliveryServiceWorkload) {
        this.destinationDistance = destinationDistance;
        this.cargoDimensions = cargoDimensions;
        this.isFragile = isFragile;
        this.deliveryServiceWorkload = deliveryServiceWorkload;
    }

    public double calculateDeliveryCost() {
        if (this.isFragile && this.destinationDistance > 30)
            throw new UnsupportedOperationException("Fragile cargo cannot be delivered for the distance more than 30");

        double calculateDeliveryCost = (getDestinationDistanceCostIncrease(this.destinationDistance) + this.cargoDimensions.getCostIncrease() +
                getFragileCostIncrease(this.isFragile)) * this.deliveryServiceWorkload.getDeliveryRate();
        DecimalFormat df = new DecimalFormat("###");
        return Math.max(Double.parseDouble(df.format(calculateDeliveryCost)), MINIMUM_DELIVERY_AMOUNT);
    }

    private int getDestinationDistanceCostIncrease(int destinationDistance) {
        if (destinationDistance > 30) return 300;
        if (destinationDistance > 10) return 200;
        if (destinationDistance > 2) return 100;
        if (destinationDistance > 0) return 50;
        throw new IllegalArgumentException("destinationDistance should be a positive number!");
    }

    private int getFragileCostIncrease(boolean isFragile) {
        return isFragile ? 300 : 0;
    }
}
