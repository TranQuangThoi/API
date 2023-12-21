package com.techmarket.api.form.cart;

import com.techmarket.api.model.CartDetail;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCartForm {

    private List<CartDetail> cartDetails;
}
