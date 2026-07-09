package com.pocketpick.salepost.domain;

import com.pocketpick.salepost.domain.domain.CardCondition;
import com.pocketpick.salepost.domain.domain.SalePostItem;
import com.pocketpick.salepost.domain.domain.exception.InvalidItemQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SalePostItem")
class SalePostItemTest {

    @Nested
    @DisplayName("생성")
    class Create {

        @Test
        @DisplayName("수량이 1 이상이면 정상 생성된다")
        void of_validQuantity_createsItem() {
            // when
            SalePostItem item = SalePostItem.of(1L, 10L, CardCondition.MINT, 3);

            // then
            assertThat(item.getCardId()).isEqualTo(10L);
            assertThat(item.getCardCondition()).isEqualTo(CardCondition.MINT);
            assertThat(item.getQuantity()).isEqualTo(3);
        }

        @Test
        @DisplayName("수량이 0이면 InvalidItemQuantityException을 던진다")
        void of_zeroQuantity_throwsException() {
            assertThatThrownBy(() -> SalePostItem.of(1L, 10L, CardCondition.MINT, 0))
                    .isInstanceOf(InvalidItemQuantityException.class);
        }

        @Test
        @DisplayName("수량이 음수이면 InvalidItemQuantityException을 던진다")
        void of_negativeQuantity_throwsException() {
            assertThatThrownBy(() -> SalePostItem.of(1L, 10L, CardCondition.MINT, -1))
                    .isInstanceOf(InvalidItemQuantityException.class);
        }
    }
}
