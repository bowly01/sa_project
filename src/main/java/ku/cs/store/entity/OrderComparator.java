package ku.cs.store.entity;

import org.springframework.stereotype.Component;

@Component
public class OrderComparator {
    public int compareByTimestamp(PurchaseOrder order1, PurchaseOrder order2) {
        return order2.getTimestamp().compareTo(order1.getTimestamp());
    }
}
