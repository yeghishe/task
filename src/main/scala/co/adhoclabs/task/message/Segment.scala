package co.adhoclabs.task.message

/**
 * Created by yeghishe on 8/3/15.
 */
case class SegmentOrderEvent(userId: String, orderId: String, items: List[SegmentOrderEventItem])
case class SegmentOrderEventItem(sku: String, name: String, price: BigDecimal, quantity: Int = 1)
