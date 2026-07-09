package com.pocketpick.salepost.domain.domain;

public enum SaleStatus {

    ON_SALE {
        @Override
        public boolean canTransitionTo(SaleStatus next) {
            return next == RESERVED || next == SOLD;
        }
    },
    RESERVED {
        @Override
        public boolean canTransitionTo(SaleStatus next) {
            return next == SOLD || next == ON_SALE;
        }
    },
    SOLD {
        @Override
        public boolean canTransitionTo(SaleStatus next) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(SaleStatus next);
}
