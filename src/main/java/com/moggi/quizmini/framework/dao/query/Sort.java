package com.moggi.quizmini.framework.dao.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public final class Sort {
    private static final long serialVersionUID = -1196053849111814317L;
    private List<Order> orders;

    public Sort(List<Order> orders) {
        this.orders = (orders == null || orders.isEmpty()) ? new ArrayList() : orders;
    }

    public Sort(Order... orders) {
        this((List<Order>) Arrays.asList(orders));
    }

    @Data
    @AllArgsConstructor
    public static class Order {
        private static final long serialVersionUID = 1;
        private String prop;
        private String order;

        public enum ORDER {
            ASC,
            DESC;

            @Override
            public String toString() {
                return name().toLowerCase();
            }
        }

        public Order() {
            this.order = ORDER.ASC.toString();
        }

    }
}