package com.evision.CartManage.Pojo

data class CHECKOUTDATA(
        var order_review: List<OrderReview>,
        var order_totals: List<OrderTotal>,
        var is_allow_bac_credomatic: Boolean,
        var status: Int
)